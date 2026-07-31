import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String expectedOwnerUrl = 'http://localhost:8080/owners/6'
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

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

def visitRow = { String dateValue, String description ->
    TestObject object = new TestObject('visitRow_' + description)
    object.addProperty('xpath', ConditionType.EQUALS,
        "//table[.//th[normalize-space(.)='Visit Date'] and .//th[normalize-space(.)='Description']]//tr[td[1][normalize-space(.)='" +
        dateValue + "'] and td[2][normalize-space(.)='" + description + "']]")
    return object
}

def addVisit = { String description ->
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(dateInput, 10)

    WebUI.executeJavaScript("""
var d = document.getElementById('date');
if (!d) {
    throw new Error('Visit Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [visitDate])

    WebUI.waitForElementVisible(descriptionInput, 10)
    WebUI.setText(descriptionInput, description)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)

    // PetClinic may append an optional ;jsessionid=... path parameter after redirect.
    // Normalize the URL before validating the stable owner details route.
    String normalizedUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(normalizedUrl, '^' + expectedOwnerUrl + '/?$', true)

    WebUI.waitForElementVisible(visitRow(visitDate, description), 10)
    WebUI.verifyElementVisible(visitRow(visitDate, description))
}

try {
    WebUI.openBrowser('')

    addVisit(firstDescription)
    addVisit(secondDescription)

    WebUI.waitForElementVisible(visitRow(visitDate, firstDescription), 10)
    WebUI.waitForElementVisible(visitRow(visitDate, secondDescription), 10)
    WebUI.verifyElementVisible(visitRow(visitDate, firstDescription))
    WebUI.verifyElementVisible(visitRow(visitDate, secondDescription))
} finally {
    WebUI.closeBrowser()
}
