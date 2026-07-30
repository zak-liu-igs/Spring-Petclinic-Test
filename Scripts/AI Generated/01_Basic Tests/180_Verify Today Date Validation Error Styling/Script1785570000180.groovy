import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String today = LocalDate.now().toString()
String description = 'Today styling ' + System.currentTimeMillis()

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='description' and @name='description']")

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='date']]//button[@type='submit' and normalize-space(.)='Add Visit']")

TestObject dateGroup = new TestObject('dateGroup')
dateGroup.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='date']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]")

TestObject dateError = new TestObject('dateError')
dateError.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='date']/following-sibling::span[contains(concat(' ', normalize-space(@class), ' '), ' help-inline ')]")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)

    WebUI.executeJavaScript("""
var d = document.getElementById('date');
if (!d) {
    throw new Error('Visit Date field was not found');
}
d.removeAttribute('min');
d.value = '${today}';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
    WebUI.setText(descriptionInput, description)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(dateError, 10)

    WebUI.verifyMatch(WebUI.getAttribute(dateGroup, 'class'), '.*\\bhas-error\\b.*', true)
    WebUI.verifyElementText(dateError, 'Visit date must be in the future')
} finally {
    WebUI.closeBrowser()
}
