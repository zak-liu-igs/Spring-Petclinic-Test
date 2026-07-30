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
    WebUI.navigateToUrl(baseUrl)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'))

    WebUI.verifyElementPresent(xpath('findOwnersHeading', "//h2[normalize-space(.)='Find Owners']"), 10)
    WebUI.verifyElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'))
    WebUI.verifyElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Find Owner'))
    WebUI.verifyElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'))
    WebUI.verifyMatch(WebUI.getUrl(), '.*/owners/find(?:\\?.*)?$', true)
} finally {
    WebUI.closeBrowser()
}
