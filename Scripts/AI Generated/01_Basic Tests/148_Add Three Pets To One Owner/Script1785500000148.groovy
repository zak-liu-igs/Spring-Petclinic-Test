import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
List<List<String>> pets = [['Alpha' + suffix, 'cat'], ['Bravo' + suffix, 'dog'], ['Charlie' + suffix, 'hamster']]

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'ThreePets')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '148 Trio Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)/
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
    }

    pets.each { List<String> pet ->
        WebUI.verifyTextPresent(pet[0], false)
        WebUI.verifyTextPresent(pet[1], false)
    }
    Number editActionCount = (Number) WebUI.executeJavaScript(
        "return document.querySelectorAll(\"a[href^='/owners/" + ownerId + "/pets/'][href\$='/edit']\").length;",
        null)
    WebUI.verifyEqual(editActionCount.intValue(), 3)
} finally {
    WebUI.closeBrowser()
}
