import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = 'UpdateOnly' + token.substring(token.length() - 6)

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'UpdateOnly')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '254 Update Only Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
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
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'hamster', false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))
    WebUI.click(xpath('edit created pet',
        "//tr[.//dd[normalize-space(.)='" + petName + "']]//a[normalize-space(.)='Edit Pet']"))

    Number updateButtons = (Number) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll(\"form button[type='submit']\"))" +
        ".filter(function(b){return b.textContent.trim()==='Update Pet';}).length;", null)
    Number addButtons = (Number) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll(\"form button[type='submit']\"))" +
        ".filter(function(b){return b.textContent.trim()==='Add Pet';}).length;", null)
    WebUI.verifyEqual(updateButtons.intValue(), 1)
    WebUI.verifyEqual(addButtons.intValue(), 0)
    WebUI.verifyTextPresent('Pet', false)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '/pets/\\d+/edit/?', true)
} finally {
    WebUI.closeBrowser()
}
