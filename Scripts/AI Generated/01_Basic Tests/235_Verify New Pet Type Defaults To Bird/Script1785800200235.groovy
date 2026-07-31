import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'DefaultType')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '235 Default Type Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))

    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')

    Map<String, Object> selected = (Map<String, Object>) WebUI.executeJavaScript(
        "var s=document.getElementById('type'); var o=s.options[s.selectedIndex];" +
        "return {text:o.textContent.trim(), value:o.value, index:s.selectedIndex};", null)
    WebUI.verifyEqual(selected.text, 'bird')
    WebUI.verifyEqual(selected.value, 'bird')
    WebUI.verifyEqual(((Number) selected.index).intValue(), 0)
    WebUI.verifyElementSelectedByValue(findTestObject(repository + 'select_Type'), 'bird', false, 10)
} finally {
    WebUI.closeBrowser()
}
