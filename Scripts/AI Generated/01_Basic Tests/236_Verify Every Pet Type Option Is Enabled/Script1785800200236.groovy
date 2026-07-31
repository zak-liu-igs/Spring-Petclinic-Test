import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'EnabledTypes')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '236 Enabled Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))

    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')

    List<Map<String, Object>> options = (List<Map<String, Object>>) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('#type option')).map(function(o){" +
        "return {text:o.textContent.trim(), value:o.value, disabled:o.disabled};});", null)
    WebUI.verifyEqual(options.size(), 6)
    WebUI.verifyEqual(options.every { Map<String, Object> option -> !((Boolean) option.disabled) }, true)
    WebUI.verifyEqual(options.every { Map<String, Object> option -> option.value.toString().length() > 0 }, true)
    WebUI.verifyEqual(options.collect { Map<String, Object> option -> option.value },
        ['bird', 'cat', 'dog', 'hamster', 'lizard', 'snake'])
} finally {
    WebUI.closeBrowser()
}
