import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)

    WebUI.verifyElementPresent(xpath('activeFindOwners',
        "//nav//a[@href='/owners/find' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('activePrimaryLinks',
        "//nav//ul[contains(concat(' ', normalize-space(@class), ' '), ' navbar-nav ')]" +
        "//a[contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10).size(), 1)
    WebUI.verifyElementNotPresent(xpath('activeHome',
        "//nav//a[@href='/' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 2)
    WebUI.verifyElementNotPresent(xpath('activeVeterinarians',
        "//nav//a[@href='/vets.html' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 2)
    WebUI.verifyElementNotPresent(xpath('activeError',
        "//nav//a[@href='/oups' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 2)
} finally {
    WebUI.closeBrowser()
}
