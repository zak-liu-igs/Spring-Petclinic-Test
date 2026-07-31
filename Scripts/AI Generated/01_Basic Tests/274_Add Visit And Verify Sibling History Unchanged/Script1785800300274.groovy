import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String baseUrl = 'http://localhost:8080'
String visitDate = LocalDate.now().plusDays(4).toString()
String description = 'Sibling isolation ' + System.currentTimeMillis()

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

TestObject maxVisitRows = xpath('maxVisitRows',
    "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]" +
    "//tr[td[1]//dd[1][normalize-space(.)='Max']]" +
    "//table[contains(concat(' ', normalize-space(@class), ' '), ' table-condensed ')]//tr[td and not(.//a)]")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/6')
    WebUI.waitForPageLoad(10)
    int maxCountBefore = WebUI.findWebElements(maxVisitRows, 10).size()
    WebUI.verifyEqual(maxCountBefore >= 2, true)

    WebUI.navigateToUrl(baseUrl + '/owners/6/pets/7/visits/new')
    WebUI.waitForPageLoad(10)
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

    WebUI.verifyEqual(WebUI.findWebElements(maxVisitRows, 10).size(), maxCountBefore)
    WebUI.verifyElementPresent(xpath('samanthaNewVisit',
        "//tr[td[1]//dd[1][normalize-space(.)='Samantha']]//tr[td[2][normalize-space(.)='" + description + "']]"), 10)
    WebUI.verifyElementNotPresent(xpath('maxNoNewVisit',
        "//tr[td[1]//dd[1][normalize-space(.)='Max']]//tr[td[2][normalize-space(.)='" + description + "']]"), 2)
} finally {
    WebUI.closeBrowser()
}
