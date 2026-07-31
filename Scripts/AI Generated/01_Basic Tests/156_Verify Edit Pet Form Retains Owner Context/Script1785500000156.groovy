import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String firstName = 'EditContext'
String lastName = 'Owner' + token.substring(token.length() - 7)
String fullName = firstName + ' ' + lastName
String petName = 'Context' + token.substring(token.length() - 6)
String birthDate = '2024-01-01'
String petType = 'lizard'

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

def setPetTypeByValue = { String typeValue ->
    WebUI.executeJavaScript("""
var s = document.getElementById('type');
if (!s) {
    throw new Error('Pet Type select was not found');
}
s.value = arguments[0];
s.dispatchEvent(new Event('input', {bubbles:true}));
s.dispatchEvent(new Event('change', {bubbles:true}));
""", [typeValue])
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
    WebUI.setText(findTestObject(repository + 'input_lastName'), lastName)
    WebUI.setText(findTestObject(repository + 'input_Address'), '156 Context Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerUrl = normalizeUrl(WebUI.getUrl())
    def ownerMatcher = ownerUrl =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    // Use the visible Add New Pet action from the owner details page instead of constructing the URL.
    WebUI.waitForElementVisible(findTestObject(repository + 'a_Add New Pet'), 10)
    WebUI.click(findTestObject(repository + 'a_Add New Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_PetName'), 10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), petName)
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [birthDate])

    WebUI.waitForElementVisible(findTestObject(repository + 'select_Type'), 10)
    // Use JavaScript to avoid intermittent click interception on the select control.
    setPetTypeByValue(petType)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyTextPresent(petName, false)
    WebUI.verifyTextPresent(petType, false)

    TestObject editPetLink = xpath('edit link for created pet',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "']]//a[normalize-space(.)='Edit Pet']")
    WebUI.waitForElementClickable(editPetLink, 10)
    WebUI.scrollToElement(editPetLink, 5)
    WebUI.click(editPetLink)
    WebUI.waitForPageLoad(10)

    String editUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(editUrl, '^' + baseUrl + '/owners/' + ownerId + '/pets/\\d+/edit/?$', true)

    // PetClinic edit-pet pages may render the heading as "Pet" rather than the literal text "Edit Pet".
    // The stable owner-context requirement is that the edit form is loaded and preserves owner/pet field values.
    WebUI.verifyElementPresent(xpath('petFormHeading', "//h2[contains(normalize-space(.), 'Pet')]"), 10)
    WebUI.verifyTextPresent(fullName, false)
    WebUI.verifyEqual(WebUI.getAttribute(findTestObject(repository + 'input_PetName'), 'value'), petName)
    WebUI.verifyEqual(WebUI.getAttribute(findTestObject(repository + 'input_Birth Date'), 'value'), birthDate)

    String selectedType = (String) WebUI.executeJavaScript(
        "var s=document.getElementById('type'); return s.options[s.selectedIndex].textContent.trim();", null)
    WebUI.verifyEqual(selectedType, petType)
} finally {
    WebUI.closeBrowser()
}
