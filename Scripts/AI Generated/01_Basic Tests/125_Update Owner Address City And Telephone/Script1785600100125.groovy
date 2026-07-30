import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())
String firstName = 'Update' + token.substring(token.length() - 5)
String lastName = 'Owner' + token.substring(token.length() - 7)
String newAddress = '125 Updated Boulevard'
String newCity = 'Updated City'
String newTelephone = '8' + token.substring(token.length() - 9)

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def detailCell = { String label ->
    xpath('detail' + label,
        "//table[contains(@class,'table-striped')]//th[normalize-space(.)='" + label + "']/following-sibling::td")
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), firstName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'), lastName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '125 Original Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Original City')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    WebUI.click(xpath('editOwner', "//a[normalize-space(.)='Edit Owner']"))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), newAddress)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), newCity)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), newTelephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Update Owner'))

    WebUI.verifyElementPresent(xpath('ownerInformation', "//h2[normalize-space(.)='Owner Information']"), 10)
    WebUI.verifyElementText(detailCell('Address'), newAddress)
    WebUI.verifyElementText(detailCell('City'), newCity)
    WebUI.verifyElementText(detailCell('Telephone'), newTelephone)
} finally {
    WebUI.closeBrowser()
}
