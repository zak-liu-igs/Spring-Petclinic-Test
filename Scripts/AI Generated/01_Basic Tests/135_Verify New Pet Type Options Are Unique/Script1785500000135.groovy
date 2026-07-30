import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'TypeList')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '135 Options Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))

    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')

    List<String> types = (List<String>) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('#type option')).map(function(o){return o.textContent.trim();});",
        null)
    List<String> expectedTypes = ['bird', 'cat', 'dog', 'hamster', 'lizard', 'snake']
    WebUI.verifyEqual(types, expectedTypes)
    WebUI.verifyEqual(types.toSet().size(), expectedTypes.size())
} finally {
    WebUI.closeBrowser()
}
