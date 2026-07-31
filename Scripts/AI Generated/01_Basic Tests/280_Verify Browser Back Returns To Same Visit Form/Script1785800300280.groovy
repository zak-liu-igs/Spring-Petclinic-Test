import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String visitUrl = baseUrl + '/owners/6/pets/8/visits/new'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Veterinarians'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyElementText(xpath('veterinariansHeading', "//h2"), 'Veterinarians')

    WebUI.back()
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), visitUrl)
    WebUI.verifyElementText(xpath('maxName',
        "//b[normalize-space(.)='Pet']/following-sibling::table[1]//tr[td]/td[1]"), 'Max')
    WebUI.verifyElementAttributeValue(xpath('maxPetId', "//form//input[@type='hidden' and @name='petId']"),
        'value', '8', 10)
    WebUI.verifyElementPresent(xpath('previousVisits', "//b[normalize-space(.)='Previous Visits']"), 10)
} finally {
    WebUI.closeBrowser()
}
