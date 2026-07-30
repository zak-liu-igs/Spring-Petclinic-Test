import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'),
        'City' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Required' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '114 City Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), '')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    WebUI.verifyElementPresent(xpath('cityError',
        "//*[@id='city']/ancestor::div[contains(@class,'form-group')][1]" +
        "//*[contains(@class,'help-inline') and normalize-space(.)!='']"), 10)
    WebUI.verifyElementAttributeValue(findTestObject(
        'Page_PetClinic  a Spring Framework demonstration/input_Address'), 'value', '114 City Road', 10)
} finally {
    WebUI.closeBrowser()
}
