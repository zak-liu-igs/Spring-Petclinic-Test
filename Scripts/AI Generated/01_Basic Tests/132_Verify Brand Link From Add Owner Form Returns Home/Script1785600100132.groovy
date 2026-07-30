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
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    TestObject brandLink = xpath('brandLink', "//a[contains(@class,'navbar-brand')]")
    WebUI.verifyElementVisible(brandLink)
    WebUI.click(brandLink)

    WebUI.verifyElementPresent(xpath('welcomeHeading', "//h2[normalize-space(.)='Welcome']"), 10)
    WebUI.verifyMatch(WebUI.getUrl(), '^http://localhost:8080/?$', true)
} finally {
    WebUI.closeBrowser()
}
