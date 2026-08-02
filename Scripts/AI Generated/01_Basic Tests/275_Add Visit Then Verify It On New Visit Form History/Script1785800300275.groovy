import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String baseUrl = 'http://localhost:8080'
String ownerId = '6'
String petId = '8'
String petName = 'Max'
String visitUrl = baseUrl + '/owners/' + ownerId + '/pets/' + petId + '/visits/new'
String visitDate = LocalDate.now().plusDays(5).toString()
String description = 'Reopened history ' + System.currentTimeMillis()

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    return object
}

def setDateById = { String id, String value ->
    WebUI.executeJavaScript("""
var d = document.getElementById(arguments[0]);
if (!d) {
    throw new Error('Date field was not found: ' + arguments[0]);
}
d.value = arguments[1];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [id, value])
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(xpath('dateInput', "//input[@id='date' and @name='date']"), 10)

    setDateById('date', visitDate)
    WebUI.setText(xpath('visit description', "//input[@id='description' and @name='description']"), description)
    WebUI.click(xpath('add visit button', "//button[normalize-space(.)='Add Visit'] | //input[(@type='submit' or @type='button') and @value='Add Visit']"))
    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)

    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), visitUrl + '(?:[;/?#].*)?$', true)

    WebUI.verifyElementPresent(xpath('reopenedVisit',
        "//b[normalize-space(.)='Previous Visits']/following-sibling::table[1]" +
        "//tr[td[1][normalize-space(.)='" + visitDate + "'] and td[2][normalize-space(.)='" + description + "']]"), 10)
    WebUI.verifyElementAttributeValue(xpath('maxPetId', "//form//input[@type='hidden' and @name='petId']"),
        'value', petId, 10)
    WebUI.verifyElementText(xpath('maxName',
        "//b[normalize-space(.)='Pet']/following-sibling::table[1]//tr[td]/td[1]"), petName)
} finally {
    WebUI.closeBrowser()
}
