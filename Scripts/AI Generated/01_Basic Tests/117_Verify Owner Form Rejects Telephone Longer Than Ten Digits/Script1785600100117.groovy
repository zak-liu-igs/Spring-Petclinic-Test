import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())
String invalidTelephone = '9' + token.substring(token.length() - 10)

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'),
        'Long' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Phone' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '117 Limit Street')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Miaoli')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        invalidTelephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    WebUI.verifyElementPresent(xpath('telephoneError',
        "//*[@id='telephone']/ancestor::div[contains(@class,'form-group')][1]" +
        "//*[contains(@class,'help-inline') and normalize-space(.)!='']"), 10)
    WebUI.verifyElementAttributeValue(findTestObject(
        'Page_PetClinic  a Spring Framework demonstration/input_Telephone'), 'value', invalidTelephone, 10)
} finally {
    WebUI.closeBrowser()
}
