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

    TestObject springLogo = xpath('springLogo',
        "//img[contains(@src,'/resources/images/spring-logo.svg')]")
    WebUI.verifyElementVisible(springLogo)
    WebUI.verifyElementAttributeValue(springLogo, 'alt', 'VMware Tanzu Logo', 10)
    WebUI.verifyElementAttributeValue(springLogo, 'class', 'logo', 10)
    WebUI.verifyMatch(WebUI.getAttribute(springLogo, 'src'),
        '^http://localhost:8080/resources/images/spring-logo\\.svg$', true)
} finally {
    WebUI.closeBrowser()
}
