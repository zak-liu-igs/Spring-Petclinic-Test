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
        'Method' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Action' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '230 Method Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
}

try {
    WebUI.openBrowser('')
    createOwner()
    String ownerUrl = WebUI.getUrl()
    String ownerId = ownerUrl.substring(ownerUrl.lastIndexOf('/') + 1)
    WebUI.click(xpath('editOwner', "//a[normalize-space(.)='Edit Owner']"))

    TestObject editForm = xpath('editOwnerForm',
        "//form[@id='add-owner-form' and .//button[normalize-space(.)='Update Owner']]")
    WebUI.verifyElementPresent(editForm, 10)
    WebUI.verifyElementAttributeValue(editForm, 'method', 'post', 10)
    WebUI.verifyElementAttributeValue(editForm, 'action',
        baseUrl + '/owners/' + ownerId + '/edit', 10)
    WebUI.verifyMatch(WebUI.getUrl(),
        '^http://localhost:8080/owners/' + ownerId + '/edit$', true)
} finally {
    WebUI.closeBrowser()
}
