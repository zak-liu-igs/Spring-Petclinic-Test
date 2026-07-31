import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())
String firstName = 'NoPersist' + token.substring(token.length() - 5)
String lastName = 'Invalid' + token.substring(token.length() - 7)
String originalAddress = '233 Original Address'
String originalTelephone = token.substring(token.length() - 10)
String attemptedAddress = '233 Must Not Persist'
String invalidTelephone = 'INVALID233'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def detailCell = { String label ->
    xpath('detail' + label,
        "//h2[normalize-space(.)='Owner Information']/following::table[1]" +
        "//th[normalize-space(.)='" + label + "']/following-sibling::td")
}

def createOwner = {
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), firstName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'), lastName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), originalAddress)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        originalTelephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
}

try {
    WebUI.openBrowser('')
    createOwner()
    String ownerUrl = WebUI.getUrl()
    WebUI.click(xpath('editOwner', "//a[normalize-space(.)='Edit Owner']"))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'),
        attemptedAddress)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        invalidTelephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Update Owner'))

    WebUI.verifyElementPresent(xpath('telephoneError',
        "//*[@id='telephone']/ancestor::div[contains(@class,'form-group')][1]" +
        "//*[contains(@class,'help-inline') and normalize-space(.)!='']"), 10)
    WebUI.verifyElementAttributeValue(findTestObject(
        'Page_PetClinic  a Spring Framework demonstration/input_Address'), 'value', attemptedAddress, 10)

    WebUI.navigateToUrl(ownerUrl)
    WebUI.verifyElementText(detailCell('Address'), originalAddress)
    WebUI.verifyElementText(detailCell('Telephone'), originalTelephone)
    WebUI.verifyElementText(detailCell('Name'), firstName + ' ' + lastName)
    WebUI.verifyTextNotPresent(attemptedAddress, false)
} finally {
    WebUI.closeBrowser()
}
