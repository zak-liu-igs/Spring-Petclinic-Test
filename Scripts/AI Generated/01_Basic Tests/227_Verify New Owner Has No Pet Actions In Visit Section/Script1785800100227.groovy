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
        'EmptyPets' + token.substring(token.length() - 5))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Section' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '227 Empty Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Chiayi')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
}

try {
    WebUI.openBrowser('')
    createOwner()

    WebUI.verifyElementPresent(xpath('petsAndVisits',
        "//h2[normalize-space(.)='Pets and Visits']"), 10)
    WebUI.verifyElementVisible(xpath('addNewPet', "//a[normalize-space(.)='Add New Pet']"))
    WebUI.verifyElementNotPresent(xpath('editPetAction',
        "//h2[normalize-space(.)='Pets and Visits']/following::a[normalize-space(.)='Edit Pet']"), 1)
    WebUI.verifyElementNotPresent(xpath('addVisitAction',
        "//h2[normalize-space(.)='Pets and Visits']/following::a[normalize-space(.)='Add Visit']"), 1)
} finally {
    WebUI.closeBrowser()
}
