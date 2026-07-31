import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())
String firstName = 'EditCity' + token.substring(token.length() - 5)
String lastName = 'Required' + token.substring(token.length() - 6)
String newAddress = '232 Retained Change Road'
String newTelephone = '8' + token.substring(token.length() - 9)

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def createOwner = {
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), firstName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'), lastName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '232 Original Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Original City')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
}

try {
    WebUI.openBrowser('')
    createOwner()
    String ownerId = WebUI.getUrl().substring(WebUI.getUrl().lastIndexOf('/') + 1)
    WebUI.click(xpath('editOwner', "//a[normalize-space(.)='Edit Owner']"))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), newAddress)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), '')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), newTelephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Update Owner'))

    WebUI.verifyElementPresent(xpath('cityError',
        "//*[@id='city']/ancestor::div[contains(@class,'form-group')][1]" +
        "//*[contains(@class,'help-inline') and normalize-space(.)!='']"), 10)
    WebUI.verifyElementAttributeValue(findTestObject(
        'Page_PetClinic  a Spring Framework demonstration/input_Address'), 'value', newAddress, 10)
    WebUI.verifyElementAttributeValue(findTestObject(
        'Page_PetClinic  a Spring Framework demonstration/input_City'), 'value', '', 10)
    WebUI.verifyElementAttributeValue(findTestObject(
        'Page_PetClinic  a Spring Framework demonstration/input_Telephone'), 'value', newTelephone, 10)
    WebUI.verifyMatch(WebUI.getUrl(),
        '^http://localhost:8080/owners/' + ownerId + '/edit$', true)
} finally {
    WebUI.closeBrowser()
}
