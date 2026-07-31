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

    TestObject nextControl = xpath('nextControl',
        "//div[span[normalize-space(.)='pages']]//a[@title='Next']")
    WebUI.verifyElementAttributeValue(nextControl, 'href', baseUrl + '/vets.html?page=2', 10)
    WebUI.verifyElementAttributeValue(nextControl, 'class', 'fa fa-step-forward', 10)
    WebUI.click(nextControl)
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), baseUrl + '/vets.html?page=2')
    WebUI.verifyEqual(WebUI.findWebElements(xpath('secondPageRows', "//table[@id='vets']/tbody/tr"), 10).size(), 1)
    WebUI.verifyElementText(xpath('secondPageName', "//table[@id='vets']/tbody/tr/td[1]"), 'Sharon Jenkins')
} finally {
    WebUI.closeBrowser()
}
