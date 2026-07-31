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
        'Style' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Actions' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '228 Style Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
}

try {
    WebUI.openBrowser('')
    createOwner()

    TestObject editOwner = xpath('editOwner', "//a[normalize-space(.)='Edit Owner']")
    TestObject addNewPet = xpath('addNewPet', "//a[normalize-space(.)='Add New Pet']")
    WebUI.verifyElementVisible(editOwner)
    WebUI.verifyElementVisible(addNewPet)
    WebUI.verifyMatch(WebUI.getAttribute(editOwner, 'class'),
        '(^|.*\\s)btn-primary(\\s.*|$)', true)
    WebUI.verifyMatch(WebUI.getAttribute(addNewPet, 'class'),
        '(^|.*\\s)btn-primary(\\s.*|$)', true)
    WebUI.verifyEqual(WebUI.getAttribute(editOwner, 'class'), WebUI.getAttribute(addNewPet, 'class'))
} finally {
    WebUI.closeBrowser()
}
