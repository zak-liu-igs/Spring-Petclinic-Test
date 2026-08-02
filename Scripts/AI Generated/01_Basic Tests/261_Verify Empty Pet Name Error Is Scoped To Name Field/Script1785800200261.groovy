import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
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
TestObject nameValidationGroup = xpath('name validation group', "//input[@id='name']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]")
TestObject nameRequiredMessage = xpath('name required message', "//input[@id='name']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]//span[normalize-space(.)='is required']")
TestObject dateRequiredMessage = xpath('date required message', "//input[@id='birthDate']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]//span[normalize-space(.)='is required']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'NameScope')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '261 Name Scope Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(buttonByText('Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerId = getOwnerIdFromCurrentUrl()
    String newPetUrl = baseUrl + '/owners/' + ownerId + '/pets/new'

    WebUI.navigateToUrl(newPetUrl)
    WebUI.waitForPageLoad(10)
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'dog', false)
    WebUI.click(buttonByText('Add Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getUrl(), newPetUrl + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(nameValidationGroup, 10)
    WebUI.verifyElementPresent(nameRequiredMessage, 10)
    WebUI.verifyElementNotPresent(dateRequiredMessage, 2)
    WebUI.verifyEqual(WebUI.getAttribute(birthDateInput, 'value'), birthDate)
} finally {
    WebUI.closeBrowser()
}
