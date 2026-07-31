import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String selectedDate = LocalDate.now().plusDays(7).toString()

TestObject dateInput = new TestObject('dateInput')
dateInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='date' and @name='date' and @type='date']")

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='description' and @name='description']")

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

TestObject descriptionError = new TestObject('descriptionError')
descriptionError.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='description']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]" +
    "//*[self::span or self::div][normalize-space(.)!='' and " +
    "(contains(concat(' ', normalize-space(@class), ' '), ' help-inline ') or " +
    " contains(concat(' ', normalize-space(@class), ' '), ' invalid-feedback ') or " +
    " contains(concat(' ', normalize-space(@class), ' '), ' text-danger '))]")

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(dateInput, 10)
    WebUI.waitForElementVisible(descriptionInput, 10)

    WebUI.executeJavaScript("""
var d = document.getElementById('date');
if (!d) {
    throw new Error('Visit Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [selectedDate])

    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(descriptionError, 10)

    // Empty Description validation should keep the user on the Add Visit form.
    // PetClinic may append ;jsessionid=..., so normalize before comparing the stable route.
    String normalizedUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(normalizedUrl, '^' + visitUrl + '/?$', true)

    WebUI.verifyMatch(WebUI.getText(descriptionError).trim(), '.*(must not be blank|must not be empty|required).*', true)
    WebUI.verifyElementAttributeValue(dateInput, 'value', selectedDate, 10)
} finally {
    WebUI.closeBrowser()
}
