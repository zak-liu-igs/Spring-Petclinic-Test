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

def createOwner = {
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'),
        'Heading' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Order' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '229 Heading Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Tainan')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
}

try {
    WebUI.openBrowser('')
    createOwner()

    WebUI.verifyElementPresent(xpath('ownerInformation',
        "//h2[normalize-space(.)='Owner Information']"), 10)
    WebUI.verifyElementPresent(xpath('petsAndVisits',
        "//h2[normalize-space(.)='Pets and Visits']"), 10)
    WebUI.verifyEqual(WebUI.executeJavaScript("""
return Array.from(document.querySelectorAll('h2'))
    .map(function(heading) { return heading.textContent.trim(); })
    .join('|');
""", null), 'Owner Information|Pets and Visits')
    WebUI.verifyEqual(WebUI.executeJavaScript(
        "return String(document.querySelectorAll('h2').length);", null), '2')
} finally {
    WebUI.closeBrowser()
}
