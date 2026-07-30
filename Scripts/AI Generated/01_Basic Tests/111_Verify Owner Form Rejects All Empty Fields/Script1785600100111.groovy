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
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    ['firstName', 'lastName', 'address', 'city', 'telephone'].each { String fieldId ->
        WebUI.verifyElementPresent(xpath('error' + fieldId,
            "//*[@id='" + fieldId + "']/ancestor::div[contains(@class,'form-group')][1]" +
            "//*[contains(@class,'help-inline') and normalize-space(.)!='']"), 10)
    }
    WebUI.verifyElementPresent(xpath('ownerHeading', "//h2[normalize-space(.)='Owner']"), 10)
    WebUI.verifyMatch(WebUI.getUrl(), '.*/owners/new$', true)
} finally {
    WebUI.closeBrowser()
}
