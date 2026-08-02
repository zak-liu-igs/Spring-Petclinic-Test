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

    // Current PetClinic renders owner pagination with lowercase "pages" and the Last control as:
    // <a class="fa fa-fast-forward" href="/owners?page={lastPage}" title="Last"></a>
    // The icon class is on the anchor itself, not on a child <span>, and the empty lastName query is omitted.
    TestObject paginationContainer = xpath('ownerPaginationContainer',
        "//div[.//span[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ:', 'abcdefghijklmnopqrstuvwxyz')='pages'] and .//a[@title='Last']]")
    TestObject lastPage = xpath('lastOwnerPage',
        "//div[.//span[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ:', 'abcdefghijklmnopqrstuvwxyz')='pages']]//a[@title='Last']")
    TestObject lastPageIcon = xpath('lastPageIcon',
        "//div[.//span[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ:', 'abcdefghijklmnopqrstuvwxyz')='pages']]" +
        "//a[@title='Last' and contains(concat(' ', normalize-space(@class), ' '), ' fa-fast-forward ')]")

    WebUI.verifyElementPresent(paginationContainer, 10)
    WebUI.verifyElementVisible(lastPage)

    String lastHref = normalizeUrl(WebUI.getAttribute(lastPage, 'href'))
    WebUI.verifyMatch(lastHref, '^' + baseUrl + '/owners\\?page=(?:[2-9]|[1-9][0-9]+)$', true)
    WebUI.verifyElementPresent(lastPageIcon, 10)
} finally {
    WebUI.closeBrowser()
}
