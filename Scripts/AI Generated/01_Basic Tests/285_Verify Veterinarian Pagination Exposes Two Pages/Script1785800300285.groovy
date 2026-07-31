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
    WebUI.navigateToUrl(baseUrl + '/vets.html')
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.findWebElements(xpath('pageOneCurrent',
        "//div[span[normalize-space(.)='pages']]/span/span[normalize-space(.)='1']"), 10).size(), 1)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('pageOneNumberLinks',
        "//div[span[normalize-space(.)='pages']]/span/a[not(@title) and starts-with(@href, '/vets.html?page=')]"), 10).size(), 1)
    WebUI.verifyElementText(xpath('pageTwoLink',
        "//div[span[normalize-space(.)='pages']]/span/a[not(@title)]"), '2')
    WebUI.verifyElementAttributeValue(xpath('pageTwoLinkHref',
        "//div[span[normalize-space(.)='pages']]/span/a[not(@title)]"), 'href', baseUrl + '/vets.html?page=2', 10)

    WebUI.navigateToUrl(baseUrl + '/vets.html?page=2')
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.findWebElements(xpath('pageTwoCurrent',
        "//div[span[normalize-space(.)='pages']]/span/span[normalize-space(.)='2']"), 10).size(), 1)
    WebUI.verifyElementText(xpath('pageOneLink',
        "//div[span[normalize-space(.)='pages']]/span/a[not(@title)]"), '1')
    WebUI.verifyElementAttributeValue(xpath('pageOneLinkHref',
        "//div[span[normalize-space(.)='pages']]/span/a[not(@title)]"), 'href', baseUrl + '/vets.html?page=1', 10)
} finally {
    WebUI.closeBrowser()
}
