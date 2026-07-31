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
    WebUI.navigateToUrl(baseUrl + '/owners?lastName=')

    TestObject nextPage = xpath('nextOwnerPage', "//a[@title='Next']")
    WebUI.verifyElementVisible(nextPage)
    WebUI.verifyMatch(WebUI.getAttribute(nextPage, 'href'),
        '^http://localhost:8080/owners\\?page=2$', true)
    WebUI.verifyElementPresent(xpath('nextPageIcon',
        "//a[@title='Next']/span[contains(concat(' ', normalize-space(@class), ' '), ' fa-step-forward ')]"), 10)
} finally {
    WebUI.closeBrowser()
}
