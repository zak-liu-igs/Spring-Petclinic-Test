import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String missingPath = '/contract-route-291'
String missingUrl = 'http://localhost:8080' + missingPath

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(missingUrl)
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), missingUrl)
    WebUI.verifyElementText(xpath('errorHeading', "//h2"), 'Something happened...')
    WebUI.verifyElementText(xpath('notFoundMessage',
        "//h2[normalize-space(.)='Something happened...']/following-sibling::p[1]/span"),
        'The requested page was not found.')
    WebUI.verifyElementText(xpath('requestedResource',
        "//h2[normalize-space(.)='Something happened...']/following-sibling::p[2]"),
        'No static resource contract-route-291.')
    WebUI.verifyElementNotPresent(xpath('internalErrorMessage',
        "//span[normalize-space(.)='An internal server error occurred.']"), 2)
} finally {
    WebUI.closeBrowser()
}
