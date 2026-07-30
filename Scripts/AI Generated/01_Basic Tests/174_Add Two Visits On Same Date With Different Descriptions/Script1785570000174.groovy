import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String stamp = System.currentTimeMillis().toString()
String visitDate = LocalDate.now().plusDays(5).toString()
String firstDescription = 'Same date first ' + stamp
String secondDescription = 'Same date second ' + stamp

TestObject dateInput = new TestObject('dateInput')
dateInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='date' and @name='date' and @type='date']")

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='description' and @name='description']")

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

TestObject firstVisitRow = new TestObject('firstVisitRow')
firstVisitRow.addProperty('xpath', ConditionType.EQUALS,
    "//table[contains(concat(' ', normalize-space(@class), ' '), ' table-condensed ')]//tr[td[1][normalize-space(.)='" +
    visitDate + "'] and td[2][normalize-space(.)='" + firstDescription + "']]")

TestObject secondVisitRow = new TestObject('secondVisitRow')
secondVisitRow.addProperty('xpath', ConditionType.EQUALS,
    "//table[contains(concat(' ', normalize-space(@class), ' '), ' table-condensed ')]//tr[td[1][normalize-space(.)='" +
    visitDate + "'] and td[2][normalize-space(.)='" + secondDescription + "']]")

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
d.value = '${visitDate}';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
    WebUI.setText(descriptionInput, firstDescription)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)
    WebUI.verifyEqual(WebUI.getUrl(), 'http://localhost:8080/owners/6')

    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(dateInput, 10)
    WebUI.executeJavaScript("""
var d = document.getElementById('date');
if (!d) {
    throw new Error('Visit Date field was not found');
}
d.value = '${visitDate}';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
    WebUI.setText(descriptionInput, secondDescription)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(firstVisitRow, 10)
    WebUI.waitForElementVisible(secondVisitRow, 10)
    WebUI.verifyElementVisible(firstVisitRow)
    WebUI.verifyElementVisible(secondVisitRow)
} finally {
    WebUI.closeBrowser()
}
