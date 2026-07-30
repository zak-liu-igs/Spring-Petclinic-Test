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
    WebUI.navigateToUrl(baseUrl + '/vets.html?page=2')

    TestObject firstControl = xpath('firstControl', "//a[@title='First']")
    TestObject previousControl = xpath('previousControl', "//a[@title='Previous']")
    WebUI.verifyElementVisible(firstControl)
    WebUI.verifyElementVisible(previousControl)
    WebUI.verifyMatch(WebUI.getAttribute(firstControl, 'href'),
        '^http://localhost:8080/vets\\.html\\?page=1$', true)
    WebUI.verifyMatch(WebUI.getAttribute(previousControl, 'href'),
        '^http://localhost:8080/vets\\.html\\?page=1$', true)
} finally {
    WebUI.closeBrowser()
}
