import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String today = LocalDate.now().toString()
String description = 'Invalid date URL ' + System.currentTimeMillis()

TestObject dateInput = new TestObject('dateInput')
dateInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='date' and @name='date' and @type='date']")

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='description' and @name='description']")

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='date']]//button[@type='submit' and normalize-space(.)='Add Visit']")

TestObject newVisitHeading = new TestObject('newVisitHeading')
newVisitHeading.addProperty('xpath', ConditionType.EQUALS,
    "//h2[contains(normalize-space(.), 'New') and contains(normalize-space(.), 'Visit')]")

TestObject dateError = new TestObject('dateError')
dateError.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='date']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]" +
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
d.removeAttribute('min');
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [today])

    WebUI.setText(descriptionInput, description)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(dateError, 10)

    // Invalid date validation should keep the user on the Add Visit form.
    // PetClinic may append ;jsessionid=..., so normalize before comparing the stable route.
    String normalizedUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(normalizedUrl, '^' + visitUrl + '/?$', true)

    WebUI.verifyElementVisible(newVisitHeading)
    WebUI.verifyMatch(WebUI.getText(dateError).trim(), '.*(Visit date must be in the future|must be a future date|must be in the future|future).*', true)
} finally {
    WebUI.closeBrowser()
}
