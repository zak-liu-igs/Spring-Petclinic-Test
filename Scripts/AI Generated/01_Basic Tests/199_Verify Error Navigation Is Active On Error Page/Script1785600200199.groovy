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
    WebUI.navigateToUrl(baseUrl + '/oups')

    WebUI.verifyElementPresent(xpath('errorHeading',
        "//h2[normalize-space(.)='Something happened...']"), 10)
    WebUI.verifyElementPresent(xpath('activeErrorNavigation',
        "//nav//a[contains(concat(' ', normalize-space(@class), ' '), ' active ')" +
        " and .//span[normalize-space(.)='Error']]"), 10)
    WebUI.verifyMatch(WebUI.getUrl(), '.*/oups$', true)
} finally {
    WebUI.closeBrowser()
}
