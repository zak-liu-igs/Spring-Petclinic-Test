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
    WebUI.navigateToUrl(baseUrl + '/owners/find')

    TestObject brandLink = xpath('navbarBrand',
        "//nav[contains(@class,'navbar')]//a[contains(concat(' ', normalize-space(@class), ' '), ' navbar-brand ')]")
    TestObject brandMark = xpath('navbarBrandMark',
        "//nav[contains(@class,'navbar')]//a[contains(concat(' ', normalize-space(@class), ' '), ' navbar-brand ')]/span")

    WebUI.verifyElementVisible(brandLink)
    WebUI.verifyElementAttributeValue(brandLink, 'href', baseUrl + '/', 10)
    WebUI.verifyElementPresent(brandMark, 10)
} finally {
    WebUI.closeBrowser()
}
