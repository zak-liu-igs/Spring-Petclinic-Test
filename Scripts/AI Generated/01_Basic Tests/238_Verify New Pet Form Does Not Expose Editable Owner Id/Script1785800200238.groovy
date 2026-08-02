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
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'BoundOwner')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '238 Binding Avenue')
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

    TestObject newPetForm = xpath('newPetForm', "//form[.//input[@id='name'] and .//input[@id='birthDate'] and .//select[@id='type']]")
    WebUI.verifyElementPresent(newPetForm, 10)

    Map<String, Object> controls = (Map<String, Object>) WebUI.executeJavaScript("""
var form = document.querySelector('form');
return {
    ownerControls: form.querySelectorAll("input[name='owner'], input[name='owner.id'], select[name='owner'], select[name='owner.id'], textarea[name='owner'], textarea[name='owner.id']").length,
    editableOwnerIdControls: form.querySelectorAll("input[name='owner.id']:not([type='hidden']), input[name='owner']:not([type='hidden']), select[name='owner'], select[name='owner.id'], textarea[name='owner'], textarea[name='owner.id']").length,
    editablePetIdControls: form.querySelectorAll("input[name='id']:not([type='hidden']), select[name='id'], textarea[name='id']").length,
    hiddenPetIdControls: form.querySelectorAll("input[name='id'][type='hidden']").length,
    visibleOwnerInputs: Array.from(form.querySelectorAll('input, select, textarea')).filter(function(el) {
        var name = el.getAttribute('name') || '';
        var type = (el.getAttribute('type') || '').toLowerCase();
        return type !== 'hidden' && (name === 'owner' || name === 'owner.id');
    }).length
};
""", null)

    WebUI.verifyEqual(((Number) controls.ownerControls).intValue(), 0)
    WebUI.verifyEqual(((Number) controls.editableOwnerIdControls).intValue(), 0)
    WebUI.verifyEqual(((Number) controls.visibleOwnerInputs).intValue(), 0)
    WebUI.verifyEqual(((Number) controls.editablePetIdControls).intValue(), 0)
    WebUI.verifyEqual(((Number) controls.hiddenPetIdControls).intValue(), 1)

    String newPetUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(newPetUrl, '^' + baseUrl + '/owners/' + ownerId + '/pets/new/?$', true)
} finally {
    WebUI.closeBrowser()
}
