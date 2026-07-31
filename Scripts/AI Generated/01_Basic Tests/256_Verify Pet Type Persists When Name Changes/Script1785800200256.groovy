import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String originalName = 'TypeBefore' + token.substring(token.length() - 6)
String updatedName = 'TypeAfter' + token.substring(token.length() - 6)

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'KeepType')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '256 Keep Type Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
    WebUI.setText(findTestObject(repository + 'input_PetName'), originalName)
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'lizard', false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))
    WebUI.click(xpath('edit created pet',
        "//tr[.//dd[normalize-space(.)='" + originalName + "']]//a[normalize-space(.)='Edit Pet']"))

    WebUI.clearText(findTestObject(repository + 'input_PetName'))
    WebUI.setText(findTestObject(repository + 'input_PetName'), updatedName)
    WebUI.click(findTestObject(repository + 'button_Update Pet'))

    WebUI.verifyElementPresent(xpath('renamed pet retaining lizard type',
        "//dl[.//dd[normalize-space(.)='" + updatedName + "']][.//dd[normalize-space(.)='lizard']]"), 10)
    WebUI.verifyTextNotPresent(originalName, true)
    WebUI.verifyTextPresent('Pet details has been edited', false)
} finally {
    WebUI.closeBrowser()
}
