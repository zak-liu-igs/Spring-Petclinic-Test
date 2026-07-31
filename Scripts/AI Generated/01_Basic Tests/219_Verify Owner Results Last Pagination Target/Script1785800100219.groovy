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

    TestObject lastPage = xpath('lastOwnerPage', "//a[@title='Last']")
    WebUI.verifyElementVisible(lastPage)
    WebUI.verifyMatch(WebUI.getAttribute(lastPage, 'href'),
        '^http://localhost:8080/owners\\?page=(?:[2-9]|[1-9][0-9]+)$', true)
    WebUI.verifyElementPresent(xpath('lastPageIcon',
        "//a[@title='Last']/span[contains(concat(' ', normalize-space(@class), ' '), ' fa-fast-forward ')]"), 10)
} finally {
    WebUI.closeBrowser()
}
