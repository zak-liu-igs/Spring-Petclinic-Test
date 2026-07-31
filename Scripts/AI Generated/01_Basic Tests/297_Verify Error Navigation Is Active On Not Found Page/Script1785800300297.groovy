import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String missingUrl = 'http://localhost:8080/contract-route-297'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(missingUrl)
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), missingUrl)
    WebUI.verifyElementText(xpath('notFoundMessage',
        "//h2/following-sibling::p[1]/span"), 'The requested page was not found.')
    WebUI.verifyElementPresent(xpath('activeError',
        "//nav//a[@href='/oups' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('activePrimaryLinks',
        "//nav//ul[contains(concat(' ', normalize-space(@class), ' '), ' navbar-nav ')]" +
        "//a[contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10).size(), 1)
    WebUI.verifyElementNotPresent(xpath('activeVeterinarians',
        "//nav//a[@href='/vets.html' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 2)
} finally {
    WebUI.closeBrowser()
}
