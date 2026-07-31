import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String firstName = 'IsoVisit'
String lastName = 'Owner' + token.substring(token.length() - 7)
String petName = 'IsoPet' + token.substring(token.length() - 6)
String petBirthDate = '2024-01-01'
String visitDate = LocalDate.now().plusDays(5).toString()
String visitDescription = 'ISO date validation ' + token

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
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

    WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
    WebUI.setText(findTestObject(repository + 'input_lastName'), lastName)
    WebUI.setText(findTestObject(repository + 'input_Address'), '175 ISO Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)

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
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'dog', false)
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
    TestObject descriptionInput = xpath('description input', "//input[@id='description' and @name='description']")
    WebUI.waitForElementVisible(visitDateInput, 10)
    setDateById('date', visitDate)
    WebUI.setText(descriptionInput, visitDescription)
    WebUI.click(findTestObject(repository + 'button_Add Visit'))
    WebUI.waitForPageLoad(10)

    String returnedUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(returnedUrl, '^' + baseUrl + '/owners/' + ownerId + '/?$', true)

    TestObject savedVisitRow = xpath('saved visit row',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "']]//table[.//th[normalize-space(.)='Visit Date'] and .//th[normalize-space(.)='Description']]//tr[td[1][normalize-space(.)='" + visitDate + "'] and td[2][normalize-space(.)='" + visitDescription + "']]")
    TestObject savedVisitDate = xpath('saved visit date',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "']]//table[.//th[normalize-space(.)='Visit Date'] and .//th[normalize-space(.)='Description']]//tr[td[2][normalize-space(.)='" + visitDescription + "']]/td[1]")

    WebUI.waitForElementVisible(savedVisitRow, 10)
    String displayedDate = WebUI.getText(savedVisitDate).trim()
    WebUI.verifyEqual(displayedDate, visitDate)
    WebUI.verifyMatch(displayedDate, '\\d{4}-\\d{2}-\\d{2}', true)
} finally {
    WebUI.closeBrowser()
}
