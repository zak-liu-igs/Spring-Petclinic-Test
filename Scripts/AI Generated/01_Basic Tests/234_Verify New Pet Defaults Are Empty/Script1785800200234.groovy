import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()

TestObject hiddenPetId = new TestObject('new pet hidden id')
hiddenPetId.addProperty('css', ConditionType.EQUALS, "form input[name='id'][type='hidden']")

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_First Name'), 10)
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'Defaults')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '234 Defaults Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)

    // PetClinic may append optional ;jsessionid=... path parameters. Normalize before extracting owner id.
    String ownerUrl = normalizeUrl(WebUI.getUrl())
    def ownerMatcher = ownerUrl =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    // Use the visible Add New Pet action from the owner page instead of constructing the URL manually.
    WebUI.waitForElementVisible(findTestObject(repository + 'a_Add New Pet'), 10)
    WebUI.click(findTestObject(repository + 'a_Add New Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_PetName'), 10)
    WebUI.waitForElementVisible(findTestObject(repository + 'input_Birth Date'), 10)

    WebUI.verifyEqual(WebUI.getAttribute(findTestObject(repository + 'input_PetName'), 'value'), '')
    WebUI.verifyEqual(WebUI.getAttribute(findTestObject(repository + 'input_Birth Date'), 'value'), '')
    WebUI.verifyElementPresent(hiddenPetId, 10)

    String hiddenIdValue = WebUI.getAttribute(hiddenPetId, 'value')
    WebUI.verifyEqual(hiddenIdValue == null || hiddenIdValue == '', true)

    String newPetUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(newPetUrl, '^' + baseUrl + '/owners/' + ownerId + '/pets/new/?$', true)
} finally {
    WebUI.closeBrowser()
}
