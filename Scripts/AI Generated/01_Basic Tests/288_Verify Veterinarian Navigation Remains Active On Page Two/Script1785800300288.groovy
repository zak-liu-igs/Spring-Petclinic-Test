import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String pageTwoUrl = 'http://localhost:8080/vets.html?page=2'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(pageTwoUrl)
    WebUI.waitForPageLoad(10)

    WebUI.verifyElementPresent(xpath('activeVeterinarians',
        "//nav//a[@href='/vets.html' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('activePrimaryLinks',
        "//nav//ul[contains(concat(' ', normalize-space(@class), ' '), ' navbar-nav ')]" +
        "//a[contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10).size(), 1)
    WebUI.verifyElementNotPresent(xpath('activeHome',
        "//nav//a[@href='/' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 2)
    WebUI.verifyElementNotPresent(xpath('activeOwners',
        "//nav//a[@href='/owners/find' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 2)
    WebUI.verifyElementPresent(xpath('currentPageTwo',
        "//div[span[normalize-space(.)='pages']]/span/span[normalize-space(.)='2']"), 10)
} finally {
    WebUI.closeBrowser()
}
