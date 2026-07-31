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

    WebUI.verifyEqual(WebUI.findWebElements(xpath('primaryLinks',
        "//nav//ul[contains(concat(' ', normalize-space(@class), ' '), ' navbar-nav ')]/li/a"), 10).size(), 4)
    WebUI.verifyElementPresent(xpath('homeLink',
        "//nav//a[@href='/' and @title='home page' and .//span[normalize-space(.)='Home']]"), 10)
    WebUI.verifyElementPresent(xpath('ownersLink',
        "//nav//a[@href='/owners/find' and @title='find owners' and .//span[normalize-space(.)='Find Owners']]"), 10)
    WebUI.verifyElementPresent(xpath('veterinariansLink',
        "//nav//a[@href='/vets.html' and @title='veterinarians' and .//span[normalize-space(.)='Veterinarians']]"), 10)
    WebUI.verifyElementPresent(xpath('errorLink',
        "//nav//a[@href='/oups' and @title='trigger a RuntimeException to see how it is handled'" +
        " and .//span[normalize-space(.)='Error']]"), 10)
} finally {
    WebUI.closeBrowser()
}
