import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())
String firstName = 'Ten' + token.substring(token.length() - 6)
String lastName = 'Digits' + token.substring(token.length() - 7)
String telephone = token.substring(token.length() - 10)

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), firstName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'), lastName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '118 Telephone Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Changhua')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), telephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    WebUI.verifyElementPresent(xpath('ownerInformation', "//h2[normalize-space(.)='Owner Information']"), 10)
    WebUI.verifyElementText(xpath('telephoneValue',
        "//table[contains(@class,'table-striped')]//th[normalize-space(.)='Telephone']/following-sibling::td"),
        telephone)
    WebUI.verifyMatch(WebUI.getUrl(), '.*/owners/\\d+$', true)
} finally {
    WebUI.closeBrowser()
}
