import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = 'TypeRevisit' + token.substring(token.length() - 6)
String birthDate = '2024-01-01'
String originalType = 'bird'
String updatedType = 'lizard'

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

def selectedTypeText = { ->
    return ((String) WebUI.executeJavaScript("return document.querySelector('#type').selectedOptions[0].textContent.trim();", null))
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'TypeRevisit')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '258 Type Revisit Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(buttonByText('Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerId = getOwnerIdFromCurrentUrl()

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
    WebUI.waitForPageLoad(10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), petName)
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [birthDate])
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), originalType, false)
    WebUI.verifyEqual(selectedTypeText(), originalType)
    WebUI.click(buttonByText('Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)

    WebUI.verifyElementPresent(xpath('created pet with original type',
        "//dl[.//dd[normalize-space(.)='" + petName + "']]" +
        "[.//dd[normalize-space(.)='" + birthDate + "']][.//dd[normalize-space(.)='" + originalType + "']]"), 10)

    WebUI.click(xpath('edit created pet',
        "//tr[.//dl[.//dd[normalize-space(.)='" + petName + "']]]//a[normalize-space(.)='Edit Pet']"))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '/pets/\\d+/edit(?:[;/?#].*)?$', true)
    WebUI.verifyEqual(selectedTypeText(), originalType)

    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), updatedType, false)
    WebUI.verifyEqual(selectedTypeText(), updatedType)
    WebUI.click(buttonByText('Update Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)

    TestObject updatedPet = xpath('revisited updated type',
        "//dl[.//dd[normalize-space(.)='" + petName + "']]" +
        "[.//dd[normalize-space(.)='" + birthDate + "']][.//dd[normalize-space(.)='" + updatedType + "']]")
    TestObject stalePetType = xpath('stale original type',
        "//dl[.//dd[normalize-space(.)='" + petName + "']][.//dd[normalize-space(.)='" + originalType + "']]")
    WebUI.verifyElementPresent(updatedPet, 10)
    WebUI.verifyElementNotPresent(stalePetType, 2)

    WebUI.navigateToUrl(baseUrl)
    WebUI.waitForPageLoad(10)
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId)
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(updatedPet, 10)
    WebUI.verifyElementNotPresent(stalePetType, 2)
} finally {
    WebUI.closeBrowser()
}
