import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String originalName = 'StableIdA' + token.substring(token.length() - 6)
String updatedName = 'StableIdB' + token.substring(token.length() - 6)

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

TestObject hiddenPetId = new TestObject('stable hidden pet id')
hiddenPetId.addProperty('css', ConditionType.EQUALS, "form input[name='id'][type='hidden']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'StableId')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '259 Stable Id Avenue')
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
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'cat', false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))

    WebUI.click(xpath('first edit',
        "//tr[.//dd[normalize-space(.)='" + originalName + "']]//a[normalize-space(.)='Edit Pet']"))
    def firstRoute = WebUI.getUrl() =~ /\/owners\/\d+\/pets\/(\d+)\/edit\/?$/
    WebUI.verifyEqual(firstRoute.find(), true)
    String petId = firstRoute.group(1)
    WebUI.verifyEqual(WebUI.getAttribute(hiddenPetId, 'value'), petId)
    WebUI.clearText(findTestObject(repository + 'input_PetName'))
    WebUI.setText(findTestObject(repository + 'input_PetName'), updatedName)
    WebUI.click(findTestObject(repository + 'button_Update Pet'))

    WebUI.click(xpath('second edit',
        "//tr[.//dd[normalize-space(.)='" + updatedName + "']]//a[normalize-space(.)='Edit Pet']"))
    def secondRoute = WebUI.getUrl() =~ /\/owners\/(\d+)\/pets\/(\d+)\/edit\/?$/
    WebUI.verifyEqual(secondRoute.find(), true)
    WebUI.verifyEqual(secondRoute.group(1), ownerId)
    WebUI.verifyEqual(secondRoute.group(2), petId)
    WebUI.verifyEqual(WebUI.getAttribute(hiddenPetId, 'value'), petId)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'snake', false)
    WebUI.click(findTestObject(repository + 'button_Update Pet'))
    WebUI.verifyElementPresent(xpath('same identifier updated record',
        "//dl[.//dd[normalize-space(.)='" + updatedName + "']][.//dd[normalize-space(.)='snake']]"), 10)
} finally {
    WebUI.closeBrowser()
}
