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
    WebUI.waitForPageLoad(10)

    // Current PetClinic renders the label as lowercase "pages" and the active page as nested spans:
    // <span><span>1</span></span>. Do not depend on the old literal "Pages:" text.
    TestObject paginationContainer = xpath('paginationContainer',
        "//div[.//span[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ:', 'abcdefghijklmnopqrstuvwxyz')='pages'] and .//a[contains(@href, '/vets.html?page=2')]]")
    TestObject currentPageText = xpath('currentPageText',
        "//div[.//span[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ:', 'abcdefghijklmnopqrstuvwxyz')='pages']]" +
        "//span[not(self::a) and not(ancestor::a) and normalize-space(.)='1']")
    TestObject currentPageLink = xpath('currentPageLink',
        "//div[.//span[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ:', 'abcdefghijklmnopqrstuvwxyz')='pages']]//a[normalize-space(.)='1']")
    TestObject otherPageLink = xpath('otherPageLink',
        "//div[.//span[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ:', 'abcdefghijklmnopqrstuvwxyz')='pages']]//a[normalize-space(.)='2']")

    WebUI.verifyElementPresent(paginationContainer, 10)
    WebUI.verifyElementPresent(currentPageText, 10)
    WebUI.verifyEqual(WebUI.findWebElements(currentPageLink, 2).size(), 0)
    WebUI.verifyElementVisible(otherPageLink)
    WebUI.verifyMatch(WebUI.getAttribute(otherPageLink, 'href'), '^' + baseUrl + '/vets.html\\?page=2$', true)
} finally {
    WebUI.closeBrowser()
}
