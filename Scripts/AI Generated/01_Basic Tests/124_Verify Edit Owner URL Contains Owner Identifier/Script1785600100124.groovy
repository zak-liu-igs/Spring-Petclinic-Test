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
        'Edit' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Identifier' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '124 Edit Lane')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taoyuan')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    String ownerUrl = WebUI.getUrl()
    String ownerId = ownerUrl.substring(ownerUrl.lastIndexOf('/') + 1)
    WebUI.click(xpath('editOwner', "//a[normalize-space(.)='Edit Owner']"))

    WebUI.verifyMatch(WebUI.getUrl(), '^http://localhost:8080/owners/' + ownerId + '/edit$', true)
    WebUI.verifyElementPresent(xpath('ownerHeading', "//h2[normalize-space(.)='Owner']"), 10)
    WebUI.verifyElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Update Owner'))
} finally {
    WebUI.closeBrowser()
}
