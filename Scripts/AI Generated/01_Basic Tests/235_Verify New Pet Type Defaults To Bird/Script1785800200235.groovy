import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_First Name'), 10)
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'DefaultType')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '235 Default Type Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)

    // PetClinic may append optional ;jsessionid=... path parameters. Normalize before extracting owner id.
    String ownerUrl = normalizeUrl(WebUI.getUrl())
    def ownerMatcher = ownerUrl =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    // Use the visible Add New Pet action from the owner details page instead of constructing the URL manually.
    WebUI.waitForElementVisible(findTestObject(repository + 'a_Add New Pet'), 10)
    WebUI.click(findTestObject(repository + 'a_Add New Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'select_Type'), 10)

    Map<String, Object> selected = (Map<String, Object>) WebUI.executeJavaScript(
        "var s=document.getElementById('type'); var o=s.options[s.selectedIndex];" +
        "return {text:o.textContent.trim(), value:o.value, index:s.selectedIndex};", null)

    WebUI.verifyEqual(selected.text, 'bird')
    WebUI.verifyEqual(selected.value, 'bird')
    WebUI.verifyEqual(((Number) selected.index).intValue(), 0)

    // Validate with JavaScript because WebUI.verifyElementSelectedByValue can be sensitive to select-object locator metadata.
    Boolean birdSelected = (Boolean) WebUI.executeJavaScript(
        "var s=document.getElementById('type'); return s && s.value === 'bird' && s.selectedIndex === 0;", null)
    WebUI.verifyEqual(birdSelected, true)

    String newPetUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(newPetUrl, '^' + baseUrl + '/owners/' + ownerId + '/pets/new/?$', true)
} finally {
    WebUI.closeBrowser()
}
