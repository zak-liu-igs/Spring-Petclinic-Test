import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = 'HiddenId' + token.substring(token.length() - 6)

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

TestObject hiddenPetId = new TestObject('edit pet hidden id')
hiddenPetId.addProperty('css', ConditionType.EQUALS, "form input[name='id'][type='hidden']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'HiddenId')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '251 Hidden Id Avenue')
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
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'cat', false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))
    WebUI.click(xpath('edit created pet',
        "//tr[.//dd[normalize-space(.)='" + petName + "']]//a[normalize-space(.)='Edit Pet']"))

    Number hiddenCount = (Number) WebUI.executeJavaScript(
        "return document.querySelectorAll(\"form input[name='id'][type='hidden']\").length;", null)
    String hiddenValue = WebUI.getAttribute(hiddenPetId, 'value')
    WebUI.verifyEqual(hiddenCount.intValue(), 1)
    WebUI.verifyNotEqual(hiddenValue, '')
    WebUI.verifyMatch(hiddenValue, '\\d+', true)
    WebUI.verifyElementPresent(hiddenPetId, 10)
} finally {
    WebUI.closeBrowser()
}
