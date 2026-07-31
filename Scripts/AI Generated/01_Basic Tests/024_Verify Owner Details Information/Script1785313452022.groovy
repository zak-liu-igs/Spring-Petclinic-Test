import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String firstName = 'DetailsF' + token.substring(token.length() - 6)
String lastName = 'DetailsL' + token.substring(token.length() - 7)
String fullName = firstName + ' ' + lastName
String address = token + ' Details Street'
String city = 'DetailsCity'
String telephone = token.substring(token.length() - 10)
String petName = 'DetailsPet' + token.substring(token.length() - 6)
String petBirthDate = '2024-01-01'
String petType = 'dog'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    return object
}

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

def setDateById = { String fieldId, String value ->
    WebUI.executeJavaScript("""
var d = document.getElementById(arguments[0]);
if (!d) {
    throw new Error('Date field was not found: ' + arguments[0]);
}
d.value = arguments[1];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [fieldId, value])
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    // Create isolated owner data instead of relying on mutable seeded Davis/Lucky records.
    WebUI.waitForElementVisible(findTestObject(repository + 'input_First Name'), 10)
    WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
    WebUI.setText(findTestObject(repository + 'input_lastName'), lastName)
    WebUI.setText(findTestObject(repository + 'input_Address'), address)
    WebUI.setText(findTestObject(repository + 'input_City'), city)
    WebUI.setText(findTestObject(repository + 'input_Telephone'), telephone)
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerUrl = normalizeUrl(WebUI.getUrl())
    def ownerMatcher = ownerUrl =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    TestObject ownerInformationTable = xpath('owner information table',
        "//h2[normalize-space(.)='Owner Information']/following::table[1][" +
        ".//tr[th[normalize-space(.)='Name'] and td/b[normalize-space(.)='" + fullName + "']] and " +
        ".//tr[th[normalize-space(.)='Address'] and td[normalize-space(.)='" + address + "']] and " +
        ".//tr[th[normalize-space(.)='City'] and td[normalize-space(.)='" + city + "']] and " +
        ".//tr[th[normalize-space(.)='Telephone'] and td[normalize-space(.)='" + telephone + "']]]")
    WebUI.verifyElementPresent(ownerInformationTable, 10)

    WebUI.waitForElementVisible(findTestObject(repository + 'a_Add New Pet'), 10)
    WebUI.click(findTestObject(repository + 'a_Add New Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_PetName'), 10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), petName)
    setDateById('birthDate', petBirthDate)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), petType, false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))
    WebUI.waitForPageLoad(10)

    String finalUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(finalUrl, '^' + baseUrl + '/owners/' + ownerId + '/?$', true)

    WebUI.verifyTextPresent('Owner Information', false)
    WebUI.verifyTextPresent(fullName, false)
    WebUI.verifyTextPresent('Address', false)
    WebUI.verifyTextPresent(address, false)
    WebUI.verifyTextPresent('City', false)
    WebUI.verifyTextPresent(city, false)
    WebUI.verifyTextPresent('Telephone', false)
    WebUI.verifyTextPresent(telephone, false)
    WebUI.verifyTextPresent('Pets and Visits', false)

    TestObject petDetailsRow = xpath('pet details row',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "'] and " +
        ".//dt[normalize-space(.)='Birth Date']/following-sibling::dd[1][normalize-space(.)='" + petBirthDate + "'] and " +
        ".//dt[normalize-space(.)='Type']/following-sibling::dd[1][normalize-space(.)='" + petType + "']]")
    WebUI.verifyElementPresent(petDetailsRow, 10)
    WebUI.verifyTextPresent(petName, false)
} finally {
    WebUI.closeBrowser()
}
