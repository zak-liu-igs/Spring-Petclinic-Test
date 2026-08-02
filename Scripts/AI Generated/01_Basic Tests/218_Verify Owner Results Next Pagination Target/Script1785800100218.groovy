import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners?lastName=')
    WebUI.waitForPageLoad(10)

    // Current PetClinic renders owner pagination with lowercase "pages" and the Next control as:
    // <a class="fa fa-step-forward" href="/owners?page=2" title="Next"></a>
    // The icon class is on the anchor itself, not on a child <span>, and the empty lastName query is omitted.
    TestObject paginationContainer = xpath('ownerPaginationContainer',
        "//div[.//span[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ:', 'abcdefghijklmnopqrstuvwxyz')='pages'] and .//a[@title='Next']]")
    TestObject nextPage = xpath('nextOwnerPage',
        "//div[.//span[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ:', 'abcdefghijklmnopqrstuvwxyz')='pages']]//a[@title='Next']")
    TestObject nextPageIcon = xpath('nextPageIcon',
        "//div[.//span[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ:', 'abcdefghijklmnopqrstuvwxyz')='pages']]" +
        "//a[@title='Next' and contains(concat(' ', normalize-space(@class), ' '), ' fa-step-forward ')]")

    WebUI.verifyElementPresent(paginationContainer, 10)
    WebUI.verifyElementVisible(nextPage)

    String nextHref = normalizeUrl(WebUI.getAttribute(nextPage, 'href'))
    WebUI.verifyMatch(nextHref, '^' + baseUrl + '/owners\\?page=2$', true)
    WebUI.verifyElementPresent(nextPageIcon, 10)
} finally {
    WebUI.closeBrowser()
}
