import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
List<String> expectedValues = ['bird', 'cat', 'dog', 'hamster', 'lizard', 'snake']

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_First Name'), 10)
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'EnabledTypes')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '236 Enabled Avenue')
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

    List<String> optionValues = (List<String>) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('#type option')).map(function(o){ return o.value; });", null)
    List<String> optionTexts = (List<String>) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('#type option')).map(function(o){ return o.textContent.trim(); });", null)
    Boolean allOptionsEnabled = (Boolean) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('#type option')).every(function(o){ return !o.disabled; });", null)
    Boolean allValuesNonEmpty = (Boolean) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('#type option')).every(function(o){ return o.value.trim().length > 0; });", null)

    WebUI.verifyEqual(optionValues.size(), expectedValues.size())
    WebUI.verifyEqual(optionValues, expectedValues)
    WebUI.verifyEqual(optionTexts, expectedValues)
    WebUI.verifyEqual(allOptionsEnabled, true)
    WebUI.verifyEqual(allValuesNonEmpty, true)

    String newPetUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(newPetUrl, '^' + baseUrl + '/owners/' + ownerId + '/pets/new/?$', true)
} finally {
    WebUI.closeBrowser()
}
