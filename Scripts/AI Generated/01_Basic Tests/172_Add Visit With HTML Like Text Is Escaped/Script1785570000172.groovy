import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String expectedOwnerUrl = 'http://localhost:8080/owners/6'
String stamp = System.currentTimeMillis().toString()
String description = '<strong>Escaped ' + stamp + '</strong> & "quoted"'

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='description' and @name='description']")

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(descriptionInput, 10)

    WebUI.setText(descriptionInput, description)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)

    // PetClinic may append an optional ;jsessionid=... path parameter after redirect.
    // Normalize it before validating the stable owner details route.
    String normalizedUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(normalizedUrl, '^' + expectedOwnerUrl + '/?$', true)

    // Verify the exact text is displayed in a visit-description cell, then verify it was escaped instead of rendered as HTML.
    Boolean isEscaped = (Boolean) WebUI.executeJavaScript("""
var expected = arguments[0];
var cells = Array.from(document.querySelectorAll('table td'));
var cell = cells.find(function (td) { return td.textContent.trim() === expected; });
return !!cell &&
       cell.querySelector('strong') === null &&
       cell.innerHTML.indexOf('&lt;strong&gt;') !== -1 &&
       cell.innerHTML.indexOf('&lt;/strong&gt;') !== -1;
""", [description])

    WebUI.verifyEqual(isEscaped, true)
} finally {
    WebUI.closeBrowser()
}
