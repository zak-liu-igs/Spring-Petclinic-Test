import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())
String firstName = 'DetailNav' + token.substring(token.length() - 5)
String lastName = 'Active' + token.substring(token.length() - 7)

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def createOwner = {
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), firstName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'), lastName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '211 Detail Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
}

try {
    WebUI.openBrowser('')
    createOwner()

    WebUI.verifyElementText(xpath('ownerName',
        "//h2[normalize-space(.)='Owner Information']/following::table[1]" +
        "//th[normalize-space(.)='Name']/following-sibling::td"), firstName + ' ' + lastName)
    WebUI.verifyEqual(WebUI.executeJavaScript(
        "return String(document.querySelectorAll('#main-navbar a.nav-link.active').length);", null), '1')
    WebUI.verifyEqual(WebUI.executeJavaScript("""
var active = document.querySelector('#main-navbar a.nav-link.active');
return active ? active.textContent.trim().replace(/\\s+/g, ' ') : '';
""", null), 'Find Owners')
} finally {
    WebUI.closeBrowser()
}
