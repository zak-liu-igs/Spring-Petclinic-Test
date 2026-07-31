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

    WebUI.verifyElementPresent(xpath('ownerHeading', "//h2[normalize-space(.)='Owner']"), 10)
    WebUI.verifyEqual(WebUI.executeJavaScript(
        "return String(document.querySelectorAll('#main-navbar a.nav-link.active').length);", null), '1')
    WebUI.verifyEqual(WebUI.executeJavaScript("""
var active = document.querySelector('#main-navbar a.nav-link.active');
return active ? active.textContent.trim().replace(/\\s+/g, ' ') : '';
""", null), 'Find Owners')
} finally {
    WebUI.closeBrowser()
}
