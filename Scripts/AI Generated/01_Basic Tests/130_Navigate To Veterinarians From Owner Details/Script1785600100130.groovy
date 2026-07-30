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
        'Vets' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Navigate' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '130 Vet Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Veterinarians'))

    WebUI.verifyElementPresent(xpath('veterinariansHeading', "//h2[normalize-space(.)='Veterinarians']"), 10)
    WebUI.verifyMatch(WebUI.getUrl(), '.*/vets(?:\\.html)?(?:\\?.*)?$', true)
} finally {
    WebUI.closeBrowser()
}
