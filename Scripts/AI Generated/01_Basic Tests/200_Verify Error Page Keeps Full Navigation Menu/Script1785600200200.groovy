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
    WebUI.verifyElementPresent(xpath('errorHeading',
        "//h2[normalize-space(.)='Something happened...']"), 10)

    WebUI.verifyElementVisible(xpath('homeNavigation', "//nav//a[@href='/']"))
    WebUI.verifyElementVisible(xpath('findOwnersNavigation', "//nav//a[@href='/owners/find']"))
    WebUI.verifyElementVisible(xpath('veterinariansNavigation', "//nav//a[@href='/vets.html']"))
    WebUI.verifyElementVisible(xpath('errorNavigation', "//nav//a[@href='/oups']"))
    WebUI.verifyEqual(WebUI.findWebElements(xpath('primaryNavigationItems',
        "//nav//ul[contains(concat(' ', normalize-space(@class), ' '), ' navbar-nav ')]/li/a"), 10).size(), 4)
} finally {
    WebUI.closeBrowser()
}
