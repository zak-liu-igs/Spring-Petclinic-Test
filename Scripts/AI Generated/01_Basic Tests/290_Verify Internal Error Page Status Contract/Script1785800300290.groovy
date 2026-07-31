import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String errorUrl = 'http://localhost:8080/oups'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(errorUrl)
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), errorUrl)
    WebUI.verifyElementText(xpath('errorHeading', "//h2"), 'Something happened...')
    WebUI.verifyElementText(xpath('internalErrorMessage',
        "//h2[normalize-space(.)='Something happened...']/following-sibling::p[1]/span"),
        'An internal server error occurred.')
    WebUI.verifyElementNotPresent(xpath('notFoundMessage',
        "//span[normalize-space(.)='The requested page was not found.']"), 2)
    WebUI.verifyElementPresent(xpath('exceptionDetail',
        "//p[contains(normalize-space(.), 'controller used to showcase what happens when an exception is thrown')]"), 10)
} finally {
    WebUI.closeBrowser()
}
