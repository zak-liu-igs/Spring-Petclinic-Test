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
    WebUI.navigateToUrl(baseUrl + '/contract-route-296')
    WebUI.waitForPageLoad(10)
    WebUI.verifyElementText(xpath('notFoundMessage',
        "//h2/following-sibling::p[1]/span"), 'The requested page was not found.')
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), baseUrl + '/owners/find')
    WebUI.verifyElementText(xpath('findOwnersHeading', "//h2"), 'Find Owners')
    WebUI.verifyElementPresent(xpath('lastNameInput', "//form//input[@id='lastName' and @name='lastName']"), 10)
    WebUI.verifyElementPresent(xpath('activeFindOwners',
        "//nav//a[@href='/owners/find' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10)
} finally {
    WebUI.closeBrowser()
}
