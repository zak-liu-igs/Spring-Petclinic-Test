import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
List<String> names = ['EditOne' + suffix, 'EditTwo' + suffix, 'EditThree' + suffix]

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'NoDuplicate')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '260 No Duplicate Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
    WebUI.setText(findTestObject(repository + 'input_PetName'), names[0])
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

    [1, 2].each { int index ->
        WebUI.click(xpath('edit current name',
            "//tr[.//dd[normalize-space(.)='" + names[index - 1] + "']]//a[normalize-space(.)='Edit Pet']"))
        WebUI.clearText(findTestObject(repository + 'input_PetName'))
        WebUI.setText(findTestObject(repository + 'input_PetName'), names[index])
        WebUI.click(findTestObject(repository + 'button_Update Pet'))
    }

    Number editActionCount = (Number) WebUI.executeJavaScript(
        "return document.querySelectorAll(\"a[href*='/pets/'][href\$='/edit']\").length;", null)
    WebUI.verifyEqual(editActionCount.intValue(), 1)
    WebUI.verifyElementPresent(xpath('single latest pet record',
        "//dl[.//dd[normalize-space(.)='" + names[2] + "']][.//dd[normalize-space(.)='dog']]"), 10)
    WebUI.verifyTextNotPresent(names[0], true)
    WebUI.verifyTextNotPresent(names[1], true)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '/?', true)
} finally {
    WebUI.closeBrowser()
}
