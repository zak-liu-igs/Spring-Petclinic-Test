import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())
String firstName = 'Rows' + token.substring(token.length() - 6)
String lastName = 'Labels' + token.substring(token.length() - 7)

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def createOwner = {
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), firstName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'), lastName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '226 Labels Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taichung')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
}

try {
    WebUI.openBrowser('')
    createOwner()

    WebUI.verifyElementPresent(xpath('ownerInformation',
        "//h2[normalize-space(.)='Owner Information']"), 10)
    WebUI.verifyEqual(WebUI.executeJavaScript("""
var table = document.querySelector('table.table-striped');
return Array.from(table.querySelectorAll('th'))
    .map(function(header) { return header.textContent.trim(); })
    .join('|');
""", null), 'Name|Address|City|Telephone')
    WebUI.verifyEqual(WebUI.executeJavaScript("""
var table = document.querySelector('table.table-striped');
return String(table.querySelectorAll('tbody tr').length);
""", null), '4')
    WebUI.verifyElementText(xpath('createdOwnerName',
        "//h2[normalize-space(.)='Owner Information']/following::table[1]" +
        "//th[normalize-space(.)='Name']/following-sibling::td"), firstName + ' ' + lastName)
} finally {
    WebUI.closeBrowser()
}
