import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
List<String> types = ['bird', 'cat', 'dog', 'hamster', 'lizard', 'snake']
List<List<String>> pets = types.collect { String type -> [type.capitalize() + suffix, type] }

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'SixTypes')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '264 Six Types Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    pets.each { List<String> pet ->
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
        WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '/?', true)
    }

    Number petEditCount = (Number) WebUI.executeJavaScript(
        "return document.querySelectorAll(\"a[href*='/pets/'][href\$='/edit']\").length;", null)
    WebUI.verifyEqual(petEditCount.intValue(), 6)
    pets.each { List<String> pet ->
        WebUI.verifyElementPresent(xpath('pet of type ' + pet[1],
            "//dl[.//dd[normalize-space(.)='" + pet[0] + "']]" +
            "[.//dd[normalize-space(.)='2024-01-01']][.//dd[normalize-space(.)='" + pet[1] + "']]"), 10)
    }
} finally {
    WebUI.closeBrowser()
}
