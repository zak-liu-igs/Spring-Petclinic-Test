import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String firstName = 'ReadOnly'
String lastName = 'Owner' + token.substring(token.length() - 7)
String fullName = firstName + ' ' + lastName

TestObject ownerText = new TestObject('read only owner context')
ownerText.addProperty('xpath', ConditionType.EQUALS,
    "//form//label[normalize-space(.)='Owner']/following-sibling::div[1]/span[normalize-space(.)='" +
    fullName + "']")

def normalizeUrl = { String url ->
    url == null ? null : url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_First Name'), 10)
    WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
    WebUI.setText(findTestObject(repository + 'input_lastName'), lastName)
    WebUI.setText(findTestObject(repository + 'input_Address'), '239 Read Only Avenue')
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

    WebUI.verifyElementVisible(ownerText)
    WebUI.verifyElementText(ownerText, fullName)

    Map<String, Object> ownerContext = (Map<String, Object>) WebUI.executeJavaScript("""
var label = Array.from(document.querySelectorAll('form label')).find(function(l) {
    return l.textContent.trim() === 'Owner';
});
if (!label) {
    return { found: false, ownerText: '', editableControls: -1 };
}
var group = label.closest('.form-group') || label.parentElement;
return {
    found: true,
    ownerText: (group.textContent || '').trim(),
    editableControls: group.querySelectorAll('input:not([type="hidden"]), select, textarea, button').length
};
""", null)

    WebUI.verifyEqual(ownerContext.found, true)
    WebUI.verifyMatch(ownerContext.ownerText.toString(), '.*' + fullName + '.*', true)
    WebUI.verifyEqual(((Number) ownerContext.editableControls).intValue(), 0)

    String newPetUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(newPetUrl, '^' + baseUrl + '/owners/' + ownerId + '/pets/new/?$', true)
} finally {
    WebUI.closeBrowser()
}
