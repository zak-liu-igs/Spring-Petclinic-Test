import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String originalName = 'BeforeBoth' + token.substring(token.length() - 6)
String updatedName = 'AfterBoth' + token.substring(token.length() - 6)
String birthDate = '2024-01-01'

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

def buttonByText = { String text ->
    return xpath('button ' + text, "//button[normalize-space(.)='" + text + "'] | //input[(@type='submit' or @type='button') and @value='" + text + "']")
}

def getOwnerIdFromCurrentUrl = { ->
    String currentUrl = WebUI.getUrl()
    def matcher = currentUrl =~ /\/owners\/(\d+)(?:[;\/?#].*)?$/
    WebUI.verifyEqual(matcher.find(), true)
    return matcher.group(1)
}

TestObject birthDateInput = xpath('birth date input', "//input[@id='birthDate' and @name='birthDate']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'KeepDate')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '255 Keep Date Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(buttonByText('Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerId = getOwnerIdFromCurrentUrl()

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
    WebUI.waitForPageLoad(10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), originalName)
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [birthDate])
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'cat', false)
    WebUI.click(buttonByText('Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)

    WebUI.verifyElementPresent(xpath('created pet details',
        "//dl[.//dd[normalize-space(.)='" + originalName + "']]" +
        "[.//dd[normalize-space(.)='" + birthDate + "']][.//dd[normalize-space(.)='cat']]"), 10)

    WebUI.click(xpath('edit created pet',
        "//tr[.//dl[.//dd[normalize-space(.)='" + originalName + "']]]//a[normalize-space(.)='Edit Pet']"))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '/pets/\\d+/edit(?:[;/?#].*)?$', true)
    WebUI.verifyEqual(WebUI.getAttribute(birthDateInput, 'value'), birthDate)

    WebUI.clearText(findTestObject(repository + 'input_PetName'))
    WebUI.setText(findTestObject(repository + 'input_PetName'), updatedName)
    WebUI.verifyEqual(WebUI.getAttribute(birthDateInput, 'value'), birthDate)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'snake', false)
    WebUI.verifyEqual(WebUI.getAttribute(birthDateInput, 'value'), birthDate)
    WebUI.click(buttonByText('Update Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(xpath('updated pet with retained date',
        "//dl[.//dd[normalize-space(.)='" + updatedName + "']]" +
        "[.//dd[normalize-space(.)='" + birthDate + "']][.//dd[normalize-space(.)='snake']]"), 10)
    WebUI.verifyTextNotPresent(originalName, true)
} finally {
    WebUI.closeBrowser()
}
