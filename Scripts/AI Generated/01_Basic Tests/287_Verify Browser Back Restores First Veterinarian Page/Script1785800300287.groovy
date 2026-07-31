import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String vetsUrl = 'http://localhost:8080/vets.html'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(vetsUrl)
    WebUI.waitForPageLoad(10)
    WebUI.click(xpath('nextControl', "//div[span[normalize-space(.)='pages']]//a[@title='Next']"))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), '.*page=2$', true)

    WebUI.back()
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), vetsUrl)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('firstPageRows', "//table[@id='vets']/tbody/tr"), 10).size(), 5)
    WebUI.verifyElementText(xpath('firstVeterinarian', "//table[@id='vets']/tbody/tr[1]/td[1]"), 'James Carter')
    WebUI.verifyElementPresent(xpath('currentPageOne',
        "//div[span[normalize-space(.)='pages']]/span/span[normalize-space(.)='1']"), 10)
} finally {
    WebUI.closeBrowser()
}
