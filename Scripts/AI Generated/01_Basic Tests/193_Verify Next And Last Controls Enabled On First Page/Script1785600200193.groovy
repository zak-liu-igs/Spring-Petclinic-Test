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
    WebUI.navigateToUrl(baseUrl + '/vets.html?page=1')

    TestObject nextControl = xpath('nextControl', "//a[@title='Next']")
    TestObject lastControl = xpath('lastControl', "//a[@title='Last']")
    WebUI.verifyElementVisible(nextControl)
    WebUI.verifyElementVisible(lastControl)
    WebUI.verifyMatch(WebUI.getAttribute(nextControl, 'href'),
        '^http://localhost:8080/vets\\.html\\?page=2$', true)
    WebUI.verifyMatch(WebUI.getAttribute(lastControl, 'href'),
        '^http://localhost:8080/vets\\.html\\?page=2$', true)
} finally {
    WebUI.closeBrowser()
}
