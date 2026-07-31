import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String firstName = 'AddPetF' + token.substring(token.length() - 6)
String lastName = 'AddPetL' + token.substring(token.length() - 7)
String fullName = firstName + ' ' + lastName
String petName = 'Lucky' + token.substring(token.length() - 6)
String birthDate = '2024-01-01'
String petType = 'dog'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    return object
}

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    // Create isolated test data instead of relying on seeded owner Davis and pet name Lucky.
    // The seeded data can change between runs, and Lucky may already exist, causing duplicate-name validation.
    WebUI.waitForElementVisible(findTestObject(repository + 'input_First Name'), 10)
    WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
    WebUI.setText(findTestObject(repository + 'input_lastName'), lastName)
    WebUI.setText(findTestObject(repository + 'input_Address'), token + ' Add Pet Street')
    WebUI.setText(findTestObject(repository + 'input_City'), 'AddPetCity')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyTextPresent(fullName, false)
    String ownerUrl = normalizeUrl(WebUI.getUrl())
    def ownerMatcher = ownerUrl =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    WebUI.waitForElementVisible(findTestObject(repository + 'a_Add New Pet'), 10)
    WebUI.click(findTestObject(repository + 'a_Add New Pet'))
    WebUI.waitForPageLoad(10)

    // On the New Pet form the pet name input has id/name 'name'. Use the pet-specific repository object.
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
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), petType, false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))
    WebUI.waitForPageLoad(10)

    String finalUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(finalUrl, '^' + baseUrl + '/owners/' + ownerId + '/?$', true)

    TestObject addedPetRow = xpath('addedPetRow',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "'] and .//dt[normalize-space(.)='Birth Date']/following-sibling::dd[1][normalize-space(.)='" + birthDate + "'] and .//dt[normalize-space(.)='Type']/following-sibling::dd[1][normalize-space(.)='" + petType + "']]")
    WebUI.verifyElementPresent(addedPetRow, 10)
    WebUI.verifyTextPresent(petName, false)
    WebUI.verifyTextPresent(petType, false)
} finally {
    WebUI.closeBrowser()
}
