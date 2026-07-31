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
    WebUI.navigateToUrl(baseUrl)

    TestObject navbar = xpath('navbar', "//nav[contains(@class,'navbar')]")
    TestObject toggler = xpath('navbarToggler',
        "//nav[contains(@class,'navbar')]//button[contains(@class,'navbar-toggler')]")
    TestObject collapseTarget = xpath('collapseTarget',
        "//*[@id='main-navbar' and contains(@class,'navbar-collapse') and contains(@class,'collapse')]")

    WebUI.verifyElementAttributeValue(navbar, 'role', 'navigation', 10)
    WebUI.verifyElementAttributeValue(toggler, 'type', 'button', 10)
    WebUI.verifyElementAttributeValue(toggler, 'data-bs-toggle', 'collapse', 10)
    WebUI.verifyElementAttributeValue(toggler, 'data-bs-target', '#main-navbar', 10)
    WebUI.verifyElementPresent(collapseTarget, 10)
} finally {
    WebUI.closeBrowser()
}
