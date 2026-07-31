import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'NameScope')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '261 Name Scope Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)
    String newPetUrl = baseUrl + '/owners/' + ownerId + '/pets/new'

    WebUI.navigateToUrl(newPetUrl)
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
    WebUI.click(findTestObject(repository + 'button_Add Pet'))

    TestObject nameGroup = xpath('name validation group',
        "//input[@id='name']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]")
    TestObject nameError = xpath('name required message',
        "//input[@id='name']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]/span[" +
        "contains(concat(' ', normalize-space(@class), ' '), ' help-inline ')]")
    TestObject dateGroup = xpath('valid date group',
        "//input[@id='birthDate']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]")
    WebUI.verifyMatch(WebUI.getUrl(), newPetUrl + '/?', true)
    WebUI.verifyMatch(WebUI.getAttribute(nameGroup, 'class'), '.*\\bhas-error\\b.*', true)
    WebUI.verifyElementText(nameError, 'is required')
    WebUI.verifyEqual(WebUI.getAttribute(dateGroup, 'class').contains('has-error'), false)
    WebUI.verifyEqual(WebUI.getAttribute(findTestObject(repository + 'input_Birth Date'), 'value'), '2024-01-01')
} finally {
    WebUI.closeBrowser()
}
