import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String selectedDate = LocalDate.now().plusDays(7).toString()

TestObject dateInput = new TestObject('dateInput')
dateInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='date' and @name='date' and @type='date']")

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

TestObject descriptionError = new TestObject('descriptionError')
descriptionError.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='description']/following-sibling::span[contains(concat(' ', normalize-space(@class), ' '), ' help-inline ')]")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(dateInput, 10)

    WebUI.executeJavaScript("""
var d = document.getElementById('date');
if (!d) {
    throw new Error('Visit Date field was not found');
}
d.value = '${selectedDate}';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(descriptionError, 10)

    WebUI.verifyEqual(WebUI.getUrl(), visitUrl)
    WebUI.verifyElementText(descriptionError, 'must not be blank')
    WebUI.verifyElementAttributeValue(dateInput, 'value', selectedDate, 10)
} finally {
    WebUI.closeBrowser()
}
