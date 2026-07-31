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
    WebUI.navigateToUrl(baseUrl + '/owners/6/pets/7/visits/new')
    WebUI.waitForPageLoad(10)
    WebUI.click(xpath('brandLink', "//nav//a[contains(concat(' ', normalize-space(@class), ' '), ' navbar-brand ') and @href='/']"))
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), baseUrl + '/')
    WebUI.verifyElementText(xpath('welcomeHeading', "//h2"), 'Welcome')
    WebUI.verifyElementPresent(xpath('activeHome',
        "//nav//a[@href='/' and contains(concat(' ', normalize-space(@class), ' '), ' active ')]"), 10)
    WebUI.verifyElementPresent(xpath('petImage', "//img[@src='/resources/images/pets.png']"), 10)
} finally {
    WebUI.closeBrowser()
}
