import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String baseUrl = 'http://localhost:8080'
String visitDate = LocalDate.now().plusDays(3).toString()
String description = 'Samantha owner row ' + System.currentTimeMillis()

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/6/pets/7/visits/new')
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(xpath('dateInput', "//input[@id='date' and @name='date']"), 10)
    WebUI.executeJavaScript("""
var d = document.getElementById('date');
if (!d) {
    throw new Error('Visit Date field was not found');
}
d.value = '${visitDate}';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
    WebUI.setText(findTestObject(
        'Page_PetClinic  a Spring Framework demonstration/Page_PetClinic  a Spring Framework demonstration/input_Description'),
        description)
    WebUI.click(findTestObject(
        'Page_PetClinic  a Spring Framework demonstration/Page_PetClinic  a Spring Framework demonstration/button_Add Visit'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), baseUrl + '/owners/6')
    WebUI.verifyElementPresent(xpath('samanthaVisit',
        "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]" +
        "//tr[td[1]//dd[1][normalize-space(.)='Samantha']]" +
        "//table[contains(concat(' ', normalize-space(@class), ' '), ' table-condensed ')]" +
        "//tr[td[1][normalize-space(.)='" + visitDate + "'] and td[2][normalize-space(.)='" + description + "']]"), 10)
    WebUI.verifyElementNotPresent(xpath('maxDoesNotHaveVisit',
        "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]" +
        "//tr[td[1]//dd[1][normalize-space(.)='Max']]//tr[td[2][normalize-space(.)='" + description + "']]"), 2)
} finally {
    WebUI.closeBrowser()
}
