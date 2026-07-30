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
    TestObject pageTwoLink = xpath('pageTwoLink',
        "//div[.//span[normalize-space(.)='Pages:']]//a[normalize-space(.)='2']")
    WebUI.verifyMatch(WebUI.getAttribute(pageTwoLink, 'href'),
        '^http://localhost:8080/vets\\.html\\?page=2$', true)

    WebUI.navigateToUrl(baseUrl + '/vets.html?page=2')
    TestObject pageOneLink = xpath('pageOneLink',
        "//div[.//span[normalize-space(.)='Pages:']]//a[normalize-space(.)='1']")
    WebUI.verifyMatch(WebUI.getAttribute(pageOneLink, 'href'),
        '^http://localhost:8080/vets\\.html\\?page=1$', true)
} finally {
    WebUI.closeBrowser()
}
