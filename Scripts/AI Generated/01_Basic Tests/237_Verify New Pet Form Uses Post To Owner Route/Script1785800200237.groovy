import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def normalizeUrl = { String url ->
    url == null ? null : url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_First Name'), 10)
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'PostRoute')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '237 Route Avenue')
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

    TestObject newPetForm = xpath('newPetForm', "//form[.//input[@name='id' and @type='hidden'] and .//input[@id='name'] and .//input[@id='birthDate'] and .//select[@id='type']]")
    WebUI.verifyElementPresent(newPetForm, 10)

    String expectedNewPetUrl = baseUrl + '/owners/' + ownerId + '/pets/new'
    String currentUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(currentUrl, '^' + expectedNewPetUrl + '/?$', true)

    Map<String, Object> formInfo = (Map<String, Object>) WebUI.executeJavaScript("""
var form = document.querySelector("form input[name='id']").form;
return {
    method: form.method.toLowerCase(),
    rawAction: form.getAttribute('action'),
    resolvedAction: form.action
};
""", null)

    String method = formInfo.method.toString()
    String rawAction = formInfo.rawAction == null ? null : formInfo.rawAction.toString()
    String resolvedAction = normalizeUrl(formInfo.resolvedAction == null ? null : formInfo.resolvedAction.toString())

    WebUI.verifyEqual(method, 'post')

    // In this PetClinic version the form may omit an explicit action attribute.
    // HTML forms with no action submit to the current URL, so that is still the correct POST owner/pet route.
    if (rawAction == null || rawAction.trim().length() == 0) {
        WebUI.verifyMatch(resolvedAction, '^' + expectedNewPetUrl + '/?$', true)
    } else {
        String normalizedRawAction = normalizeUrl(rawAction)
        if (normalizedRawAction.startsWith('http')) {
            WebUI.verifyMatch(normalizedRawAction, '^' + expectedNewPetUrl + '/?$', true)
        } else {
            WebUI.verifyMatch(normalizedRawAction, '^/owners/' + ownerId + '/pets/new/?$', true)
        }
    }
} finally {
    WebUI.closeBrowser()
}
