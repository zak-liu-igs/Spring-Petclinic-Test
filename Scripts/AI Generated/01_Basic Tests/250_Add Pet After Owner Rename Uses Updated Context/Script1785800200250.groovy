import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String oldLastName = 'Owner' + token.substring(token.length() - 7)
String oldFullName = 'BeforeRename ' + oldLastName
String updatedFirstName = 'AfterRename'
String updatedLastName = oldLastName
String updatedFullName = updatedFirstName + ' ' + updatedLastName
String petName = 'AfterPet' + token.substring(token.length() - 6)

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

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'BeforeRename')
    WebUI.setText(findTestObject(repository + 'input_lastName'), oldLastName)
    WebUI.setText(findTestObject(repository + 'input_Address'), '250 Rename Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(buttonByText('Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerId = getOwnerIdFromCurrentUrl()

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/edit')
    WebUI.waitForPageLoad(10)
    WebUI.clearText(findTestObject(repository + 'input_First Name'))
    WebUI.setText(findTestObject(repository + 'input_First Name'), updatedFirstName)
    WebUI.clearText(findTestObject(repository + 'input_lastName'))
    WebUI.setText(findTestObject(repository + 'input_lastName'), updatedLastName)
    WebUI.click(buttonByText('Update Owner'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
    WebUI.verifyTextPresent(updatedFullName, true)
    WebUI.verifyTextNotPresent(oldFullName, true)

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
    WebUI.waitForPageLoad(10)
    WebUI.verifyTextPresent(updatedFullName, true)
    WebUI.verifyTextNotPresent(oldFullName, true)
    WebUI.setText(findTestObject(repository + 'input_PetName'), petName)
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'cat', false)
    WebUI.click(buttonByText('Add Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
    WebUI.verifyTextPresent(updatedFullName, true)
    WebUI.verifyTextNotPresent(oldFullName, true)
    WebUI.verifyElementPresent(xpath('pet under renamed owner',
        "//dl[.//dd[normalize-space(.)='" + petName + "']]" +
        "[.//dd[normalize-space(.)='2024-01-01']][.//dd[normalize-space(.)='cat']]"), 10)
} finally {
    WebUI.closeBrowser()
}
