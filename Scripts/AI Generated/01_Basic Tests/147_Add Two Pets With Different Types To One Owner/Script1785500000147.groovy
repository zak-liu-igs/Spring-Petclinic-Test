import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String firstPet = 'Finch' + token.substring(token.length() - 6)
String secondPet = 'Gecko' + token.substring(token.length() - 6)

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'TwoPets')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '147 Pair Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    [[firstPet, 'bird'], [secondPet, 'lizard']].each { List<String> pet ->
        WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
        WebUI.setText(findTestObject(repository + 'input_PetName'), pet[0])
        WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
        WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), pet[1], false)
        WebUI.click(findTestObject(repository + 'button_Add Pet'))
    }

    WebUI.verifyElementPresent(xpath('first pet',
        "//dl[.//dd[normalize-space(.)='" + firstPet + "']][.//dd[normalize-space(.)='bird']]"), 10)
    WebUI.verifyElementPresent(xpath('second pet',
        "//dl[.//dd[normalize-space(.)='" + secondPet + "']][.//dd[normalize-space(.)='lizard']]"), 10)
} finally {
    WebUI.closeBrowser()
}
