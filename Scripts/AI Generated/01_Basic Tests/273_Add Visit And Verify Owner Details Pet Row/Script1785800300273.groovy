import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String baseUrl = 'http://localhost:8080'
String ownerId = '6'
String petId = '7'
String petName = 'Samantha'
String otherPetName = 'Max'
String visitDate = LocalDate.now().plusDays(3).toString()
String description = 'Samantha owner row ' + System.currentTimeMillis()

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
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/' + petId + '/visits/new')
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(xpath('dateInput', "//input[@id='date' and @name='date']"), 10)

    setDateById('date', visitDate)
    WebUI.setText(xpath('visit description', "//input[@id='description' and @name='description']"), description)
    WebUI.click(xpath('add visit button', "//button[normalize-space(.)='Add Visit'] | //input[(@type='submit' or @type='button') and @value='Add Visit']"))
    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(xpath('samanthaVisit',
        "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]" +
        "//tr[td[1]//dd[normalize-space(.)='" + petName + "']]" +
        "//table[contains(concat(' ', normalize-space(@class), ' '), ' table-condensed ')]" +
        "//tr[td[1][normalize-space(.)='" + visitDate + "'] and td[2][normalize-space(.)='" + description + "']]"), 10)
    WebUI.verifyElementNotPresent(xpath('maxDoesNotHaveVisit',
        "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]" +
        "//tr[td[1]//dd[normalize-space(.)='" + otherPetName + "']]" +
        "//tr[td[2][normalize-space(.)='" + description + "']]"), 2)
} finally {
    WebUI.closeBrowser()
}
