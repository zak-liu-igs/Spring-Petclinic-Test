import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())
String firstName = 'Refresh' + token.substring(token.length() - 5)
String lastName = 'Persist' + token.substring(token.length() - 6)
String address = '126 Persistence Road'
String city = 'Yilan'
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
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), address)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), city)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), telephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    String ownerUrl = WebUI.getUrl()
    WebUI.refresh()

    WebUI.verifyEqual(WebUI.getUrl(), ownerUrl)
    WebUI.verifyElementText(xpath('ownerName',
        "//table[contains(@class,'table-striped')]//th[normalize-space(.)='Name']/following-sibling::td"),
        firstName + ' ' + lastName)
    WebUI.verifyElementText(xpath('ownerAddress',
        "//table[contains(@class,'table-striped')]//th[normalize-space(.)='Address']/following-sibling::td"),
        address)
    WebUI.verifyElementText(xpath('ownerCity',
        "//table[contains(@class,'table-striped')]//th[normalize-space(.)='City']/following-sibling::td"),
        city)
    WebUI.verifyElementText(xpath('ownerTelephone',
        "//table[contains(@class,'table-striped')]//th[normalize-space(.)='Telephone']/following-sibling::td"),
        telephone)
} finally {
    WebUI.closeBrowser()
}
