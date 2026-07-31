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

Map<String, String> labelsByField = [
    'firstName': 'First Name',
    'lastName': 'Last Name',
    'address': 'Address',
    'city': 'City',
    'telephone': 'Telephone'
]

def createOwner = {
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'),
        'EditLabel' + token.substring(token.length() - 5))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Reference' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '231 Label Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
}

try {
    WebUI.openBrowser('')
    createOwner()
    WebUI.click(xpath('editOwner', "//a[normalize-space(.)='Edit Owner']"))

    labelsByField.each { String fieldId, String labelText ->
        WebUI.verifyElementPresent(xpath('editLabelFor' + fieldId,
            "//form[@id='add-owner-form']//label[@for='" + fieldId +
            "' and normalize-space(.)='" + labelText + "']"), 10)
        WebUI.verifyElementPresent(xpath('editInputFor' + fieldId,
            "//form[@id='add-owner-form']//*[@id='" + fieldId + "' and @name='" + fieldId + "']"), 10)
    }
    WebUI.verifyElementVisible(findTestObject(
        'Page_PetClinic  a Spring Framework demonstration/button_Update Owner'))
} finally {
    WebUI.closeBrowser()
}
