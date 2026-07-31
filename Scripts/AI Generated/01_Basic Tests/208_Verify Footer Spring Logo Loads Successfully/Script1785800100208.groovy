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

    TestObject springLogo = xpath('springLogo',
        "//img[contains(@src,'/resources/images/spring-logo.svg')]")
    WebUI.verifyElementVisible(springLogo)
    WebUI.verifyEqual(WebUI.executeJavaScript("""
var logo = document.querySelector("img[src*='/resources/images/spring-logo.svg']");
return Boolean(logo && logo.complete && logo.naturalWidth > 0 && logo.naturalHeight > 0);
""", null), true)
    WebUI.verifyElementAttributeValue(springLogo, 'alt', 'VMware Tanzu Logo', 10)
} finally {
    WebUI.closeBrowser()
}
