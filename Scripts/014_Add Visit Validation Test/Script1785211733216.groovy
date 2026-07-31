import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String firstName = 'VisitValF' + token.substring(token.length() - 6)
String lastName = 'VisitValL' + token.substring(token.length() - 7)
String fullName = firstName + ' ' + lastName
String petName = 'VisitValPet' + token.substring(token.length() - 6)
String petBirthDate = '2024-01-01'
String petType = 'dog'
String visitDate = LocalDate.now().plusDays(5).toString()

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

    // Create isolated owner and pet data instead of relying on seeded Davis data and a brittle Add Visit object.
    WebUI.waitForElementVisible(findTestObject(repository + 'input_First Name'), 10)
    WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
    WebUI.setText(findTestObject(repository + 'input_lastName'), lastName)
    WebUI.setText(findTestObject(repository + 'input_Address'), token + ' Visit Validation Street')
    WebUI.setText(findTestObject(repository + 'input_City'), 'VisitValidationCity')
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

    WebUI.waitForElementVisible(findTestObject(repository + 'input_PetName'), 10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), petName)
    setDateById('birthDate', petBirthDate)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), petType, false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyTextPresent(petName, false)

    TestObject addVisitLink = xpath('add visit link for created pet',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "']]//a[normalize-space(.)='Add Visit']")
    WebUI.waitForElementClickable(addVisitLink, 10)
    WebUI.scrollToElement(addVisitLink, 5)
    WebUI.click(addVisitLink)
    WebUI.waitForPageLoad(10)

    TestObject visitDateInput = xpath('visit date input', "//input[@id='date' and @name='date' and @type='date']")
    TestObject addVisitButton = xpath('add visit button', "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")
    TestObject descriptionError = xpath('description validation error',
        "//input[@id='description']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]" +
        "//*[self::span or self::div][normalize-space(.)!='' and " +
        "(contains(concat(' ', normalize-space(@class), ' '), ' help-inline ') or " +
        " contains(concat(' ', normalize-space(@class), ' '), ' invalid-feedback ') or " +
        " contains(concat(' ', normalize-space(@class), ' '), ' text-danger '))]")

    WebUI.waitForElementVisible(visitDateInput, 10)
    setDateById('date', visitDate)

    // Leave Description empty and submit to validate required-description feedback.
    WebUI.waitForElementClickable(addVisitButton, 10)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)

    String validationUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(validationUrl, '^' + baseUrl + '/owners/' + ownerId + '/pets/\\d+/visits/new/?$', true)
    WebUI.waitForElementVisible(descriptionError, 10)
    WebUI.verifyMatch(WebUI.getText(descriptionError).trim(), '.*(must not be blank|must not be empty|required).*', true)
    WebUI.verifyElementAttributeValue(visitDateInput, 'value', visitDate, 10)
} finally {
    WebUI.closeBrowser()
}
