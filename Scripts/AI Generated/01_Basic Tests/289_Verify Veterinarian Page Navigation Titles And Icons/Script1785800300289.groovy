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

    WebUI.verifyElementPresent(xpath('disabledFirst',
        "//div[span[normalize-space(.)='pages']]//span[@title='First' and contains(@class, 'fa-fast-backward')]"), 10)
    WebUI.verifyElementPresent(xpath('disabledPrevious',
        "//div[span[normalize-space(.)='pages']]//span[@title='Previous' and contains(@class, 'fa-step-backward')]"), 10)
    WebUI.verifyElementPresent(xpath('enabledNext',
        "//div[span[normalize-space(.)='pages']]//a[@title='Next' and contains(@class, 'fa-step-forward')]"), 10)
    WebUI.verifyElementPresent(xpath('enabledLast',
        "//div[span[normalize-space(.)='pages']]//a[@title='Last' and contains(@class, 'fa-fast-forward')]"), 10)

    WebUI.navigateToUrl(baseUrl + '/vets.html?page=2')
    WebUI.waitForPageLoad(10)

    WebUI.verifyElementPresent(xpath('enabledFirst',
        "//div[span[normalize-space(.)='pages']]//a[@title='First' and contains(@class, 'fa-fast-backward')]"), 10)
    WebUI.verifyElementPresent(xpath('enabledPrevious',
        "//div[span[normalize-space(.)='pages']]//a[@title='Previous' and contains(@class, 'fa-step-backward')]"), 10)
    WebUI.verifyElementPresent(xpath('disabledNext',
        "//div[span[normalize-space(.)='pages']]//span[@title='Next' and contains(@class, 'fa-step-forward')]"), 10)
    WebUI.verifyElementPresent(xpath('disabledLast',
        "//div[span[normalize-space(.)='pages']]//span[@title='Last' and contains(@class, 'fa-fast-forward')]"), 10)
} finally {
    WebUI.closeBrowser()
}
