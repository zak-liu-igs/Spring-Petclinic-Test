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
        'Find' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Navigate' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '131 Find Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'))

    WebUI.verifyElementPresent(xpath('findOwnersHeading', "//h2[normalize-space(.)='Find Owners']"), 10)
    WebUI.verifyElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'))
    WebUI.verifyMatch(WebUI.getUrl(), '.*/owners/find$', true)
} finally {
    WebUI.closeBrowser()
}
