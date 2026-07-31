import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.time.LocalDate

String baseUrl = 'http://localhost:8080'
String visitUrl = baseUrl + '/owners/6/pets/8/visits/new'
String visitDate = LocalDate.now().plusDays(5).toString()
String description = 'Reopened history ' + System.currentTimeMillis()

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
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

    WebUI.verifyEqual(WebUI.getUrl(), baseUrl + '/owners/6')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)

    WebUI.verifyElementPresent(xpath('reopenedVisit',
        "//b[normalize-space(.)='Previous Visits']/following-sibling::table[1]" +
        "//tr[td[1][normalize-space(.)='" + visitDate + "'] and td[2][normalize-space(.)='" + description + "']]"), 10)
    WebUI.verifyElementAttributeValue(xpath('maxPetId', "//form//input[@type='hidden' and @name='petId']"),
        'value', '8', 10)
    WebUI.verifyElementText(xpath('maxName',
        "//b[normalize-space(.)='Pet']/following-sibling::table[1]//tr[td]/td[1]"), 'Max')
} finally {
    WebUI.closeBrowser()
}
