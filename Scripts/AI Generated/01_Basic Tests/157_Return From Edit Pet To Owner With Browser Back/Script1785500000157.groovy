import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = 'BackPet' + token.substring(token.length() - 6)
String petType = 'cat'
String birthDate = '2024-01-01'

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

    WebUI.setText(findTestObject(repository + 'input_First Name'), 'BackCheck')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '157 History Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerUrl = normalizeUrl(WebUI.getUrl())
    def ownerMatcher = ownerUrl =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    // Use the visible Add New Pet link so browser history contains the real owner page before the edit page.
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
    // Avoid intermittent select-click interception by setting the value directly and dispatching change events.
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
    WebUI.verifyElementPresent(xpath('petFormHeading', "//h2[contains(normalize-space(.), 'Pet')]"), 10)
    WebUI.verifyEqual(WebUI.getAttribute(findTestObject(repository + 'input_PetName'), 'value'), petName)

    WebUI.back()
    WebUI.waitForPageLoad(10)

    String returnedUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(returnedUrl, '^' + baseUrl + '/owners/' + ownerId + '/?$', true)

    TestObject returnedPetRow = xpath('returned owner pet row',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "'] and .//dt[normalize-space(.)='Type']/following-sibling::dd[1][normalize-space(.)='" + petType + "']]")
    WebUI.verifyElementPresent(returnedPetRow, 10)
    WebUI.verifyTextPresent(petName, false)
    WebUI.verifyTextPresent(petType, false)
} finally {
    WebUI.closeBrowser()
}
