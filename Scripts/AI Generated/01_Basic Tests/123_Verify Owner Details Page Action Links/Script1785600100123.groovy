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
        'Actions' + token.substring(token.length() - 5))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Links' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '123 Action Street')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    String ownerUrl = WebUI.getUrl()
    String ownerId = ownerUrl.substring(ownerUrl.lastIndexOf('/') + 1)
    TestObject editOwner = xpath('editOwner', "//a[normalize-space(.)='Edit Owner']")
    TestObject addNewPet = xpath('addNewPet', "//a[normalize-space(.)='Add New Pet']")
    WebUI.verifyElementVisible(editOwner)
    WebUI.verifyElementVisible(addNewPet)
    WebUI.verifyMatch(WebUI.getAttribute(editOwner, 'href'),
        '^http://localhost:8080/owners/' + ownerId + '/edit$', true)
    WebUI.verifyMatch(WebUI.getAttribute(addNewPet, 'href'),
        '^http://localhost:8080/owners/' + ownerId + '/pets/new$', true)
} finally {
    WebUI.closeBrowser()
}
