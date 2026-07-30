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
    WebUI.navigateToUrl(baseUrl + '/vets.html?page=2')

    WebUI.verifyElementPresent(xpath('disabledNext', "//span[@title='Next']"), 10)
    WebUI.verifyElementPresent(xpath('disabledLast', "//span[@title='Last']"), 10)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('enabledNext', "//a[@title='Next']"), 2).size(), 0)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('enabledLast', "//a[@title='Last']"), 2).size(), 0)
} finally {
    WebUI.closeBrowser()
}
