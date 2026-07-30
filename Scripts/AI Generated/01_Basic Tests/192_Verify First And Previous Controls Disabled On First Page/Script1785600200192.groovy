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

    WebUI.verifyElementPresent(xpath('disabledFirst', "//span[@title='First']"), 10)
    WebUI.verifyElementPresent(xpath('disabledPrevious', "//span[@title='Previous']"), 10)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('enabledFirst', "//a[@title='First']"), 2).size(), 0)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('enabledPrevious', "//a[@title='Previous']"), 2).size(), 0)
} finally {
    WebUI.closeBrowser()
}
