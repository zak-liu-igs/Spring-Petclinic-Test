import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
String sharedPetName = 'Shared' + suffix
List<String> ownerIds = []

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

try {
    WebUI.openBrowser('')
    ['EastEdit', 'WestKeep'].eachWithIndex { String firstName, int index ->
        WebUI.navigateToUrl(baseUrl + '/owners/new')
        WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
        WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + suffix + index)
        WebUI.setText(findTestObject(repository + 'input_Address'), (266 + index) + ' Shared Name Avenue')
        WebUI.setText(findTestObject(repository + 'input_City'), index == 0 ? 'Taipei' : 'Tainan')
        WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 9) + index)
        WebUI.click(findTestObject(repository + 'button_Add Owner'))
        def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
        WebUI.verifyEqual(ownerMatcher.find(), true)
        ownerIds.add(ownerMatcher.group(1))

        WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[index] + '/pets/new')
        WebUI.setText(findTestObject(repository + 'input_PetName'), sharedPetName)
        WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
        WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), index == 0 ? 'cat' : 'dog', false)
        WebUI.click(findTestObject(repository + 'button_Add Pet'))
    }

    WebUI.verifyNotEqual(ownerIds[0], ownerIds[1])
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[0])
    WebUI.click(xpath('edit first owner shared pet',
        "//tr[.//dd[normalize-space(.)='" + sharedPetName + "']]//a[normalize-space(.)='Edit Pet']"))
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'snake', false)
    WebUI.click(findTestObject(repository + 'button_Update Pet'))

    WebUI.verifyElementPresent(xpath('edited first owner pet',
        "//dl[.//dd[normalize-space(.)='" + sharedPetName + "']][.//dd[normalize-space(.)='snake']]"), 10)
    WebUI.verifyElementNotPresent(xpath('stale first owner type',
        "//dl[.//dd[normalize-space(.)='" + sharedPetName + "']][.//dd[normalize-space(.)='cat']]"), 2)

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[1])
    WebUI.verifyElementPresent(xpath('unchanged second owner pet',
        "//dl[.//dd[normalize-space(.)='" + sharedPetName + "']][.//dd[normalize-space(.)='dog']]"), 10)
    WebUI.verifyElementNotPresent(xpath('leaked first owner update',
        "//dl[.//dd[normalize-space(.)='" + sharedPetName + "']][.//dd[normalize-space(.)='snake']]"), 2)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerIds[1] + '/?', true)
} finally {
    WebUI.closeBrowser()
}
