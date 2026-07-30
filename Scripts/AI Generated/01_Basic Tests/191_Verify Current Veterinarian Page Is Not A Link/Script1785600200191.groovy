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

    TestObject currentPageText = xpath('currentPageText',
        "//div[.//span[normalize-space(.)='Pages:']]//span[normalize-space(.)='1' and not(*)]")
    TestObject currentPageLink = xpath('currentPageLink',
        "//div[.//span[normalize-space(.)='Pages:']]//a[normalize-space(.)='1']")
    TestObject otherPageLink = xpath('otherPageLink',
        "//div[.//span[normalize-space(.)='Pages:']]//a[normalize-space(.)='2']")

    WebUI.verifyElementPresent(currentPageText, 10)
    WebUI.verifyEqual(WebUI.findWebElements(currentPageLink, 2).size(), 0)
    WebUI.verifyElementVisible(otherPageLink)
} finally {
    WebUI.closeBrowser()
}
