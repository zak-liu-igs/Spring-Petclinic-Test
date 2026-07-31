import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = 'Actions' + token.substring(token.length() - 6)

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.setText(findTestObject(repository + 'input_First Name'), 'PetActions')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '146 Actions Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerUrl = normalizeUrl(WebUI.getUrl())
    def ownerMatcher = ownerUrl =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    // Use the application action link from the owner details page instead of constructing a URL.
    // PetClinic can render session path parameters, and relative link formats vary by version.
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
""", ['2024-01-01'])
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'bird', false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyTextPresent(petName, false)

    // Anchor the action checks to the newly added pet row. Do not require links to start with /owners/{id},
    // because PetClinic commonly renders owner-detail pet links as relative paths such as {ownerId}/pets/{petId}/edit.
    TestObject addedPetRow = xpath('added pet row',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "']]")
    TestObject editLink = xpath('pet edit action',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "']]//a[normalize-space(.)='Edit Pet']")
    TestObject visitLink = xpath('pet visit action',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "']]//a[normalize-space(.)='Add Visit']")

    WebUI.verifyElementVisible(addedPetRow)
    WebUI.verifyElementClickable(editLink)
    WebUI.verifyElementClickable(visitLink)

    String editHref = normalizeUrl(WebUI.getAttribute(editLink, 'href'))
    String visitHref = normalizeUrl(WebUI.getAttribute(visitLink, 'href'))
    WebUI.verifyMatch(editHref, '^' + baseUrl + '/owners/' + ownerId + '/pets/\\d+/edit$', true)
    WebUI.verifyMatch(visitHref, '^' + baseUrl + '/owners/' + ownerId + '/pets/\\d+/visits/new$', true)
} finally {
    WebUI.closeBrowser()
}
