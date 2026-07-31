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
    WebUI.navigateToUrl(baseUrl + '/oups')
    WebUI.waitForPageLoad(10)
    WebUI.verifyElementPresent(xpath('activeError',
        "//nav//a[@href='/oups' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Veterinarians'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getUrl(), '.*/vets\\.html$', true)
    WebUI.verifyElementPresent(xpath('activeVeterinarians',
        "//nav//a[@href='/vets.html' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10)
    WebUI.verifyElementNotPresent(xpath('inactiveError',
        "//nav//a[@href='/oups' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 2)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('activePrimaryLinks',
        "//nav//ul[contains(concat(' ', normalize-space(@class), ' '), ' navbar-nav ')]" +
        "//a[contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10).size(), 1)
} finally {
    WebUI.closeBrowser()
}
