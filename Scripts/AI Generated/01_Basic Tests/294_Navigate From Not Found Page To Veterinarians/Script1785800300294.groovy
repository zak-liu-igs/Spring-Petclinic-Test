import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/contract-route-294')
    WebUI.waitForPageLoad(10)
    WebUI.verifyElementText(xpath('notFoundMessage',
        "//h2/following-sibling::p[1]/span"), 'The requested page was not found.')
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Veterinarians'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getUrl(), '.*/vets\\.html$', true)
    WebUI.verifyElementText(xpath('veterinariansHeading', "//h2"), 'Veterinarians')
    WebUI.verifyElementPresent(xpath('activeVeterinarians',
        "//nav//a[@href='/vets.html' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10)
    WebUI.verifyElementPresent(xpath('veterinariansRows', "//table[@id='vets']/tbody/tr"), 10)
} finally {
    WebUI.closeBrowser()
}
