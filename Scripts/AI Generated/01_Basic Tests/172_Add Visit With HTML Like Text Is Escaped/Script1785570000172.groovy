import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.util.Arrays

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String stamp = System.currentTimeMillis().toString()
String description = '<strong>Escaped ' + stamp + '</strong> & "quoted"'

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='description' and @name='description']")

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(descriptionInput, 10)

    WebUI.setText(descriptionInput, description)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), 'http://localhost:8080/owners/6')
    WebUI.verifyTextPresent(description, false)

    Boolean isEscaped = WebUI.executeJavaScript("""
var expected = arguments[0];
var cells = Array.from(document.querySelectorAll('table.table-condensed td'));
var cell = cells.find(function (td) { return td.textContent.trim() === expected; });
return !!cell && cell.querySelector('strong') === null &&
       cell.innerHTML.indexOf('&lt;strong&gt;') !== -1;
""", Arrays.asList(description))

    WebUI.verifyEqual(isEscaped, true)
} finally {
    WebUI.closeBrowser()
}
