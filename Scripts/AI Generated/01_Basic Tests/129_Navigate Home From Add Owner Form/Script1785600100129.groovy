import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

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
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.verifyElementPresent(xpath('ownerHeading', "//h2[normalize-space(.)='Owner']"), 10)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Home'))

    WebUI.verifyElementPresent(xpath('welcomeHeading', "//h2[normalize-space(.)='Welcome']"), 10)
    WebUI.verifyMatch(WebUI.getUrl(), '^http://localhost:8080/?$', true)
} finally {
    WebUI.closeBrowser()
}
