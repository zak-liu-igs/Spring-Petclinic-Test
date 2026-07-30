import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())
String suffix = token.substring(token.length() - 7)
String sharedPrefix = 'Partial' + suffix
String firstLastName = sharedPrefix + 'Alpha'
String secondLastName = sharedPrefix + 'Beta'
String telephone = token.substring(token.length() - 10)

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def createOwner = { String firstName, String lastName, String address ->
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), firstName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'), lastName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), address)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taichung')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), telephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
    WebUI.verifyElementPresent(xpath('createdOwnerHeading', "//h2[normalize-space(.)='Owner Information']"), 10)
}

try {
    WebUI.openBrowser('')
    createOwner('PartialOne', firstLastName, '104 Alpha Road')
    createOwner('PartialTwo', secondLastName, '104 Beta Road')

    WebUI.navigateToUrl(baseUrl + '/owners/find')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), sharedPrefix)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Find Owner'))

    WebUI.verifyElementPresent(xpath('ownersHeading', "//h2[normalize-space(.)='Owners']"), 10)
    WebUI.verifyElementPresent(xpath('firstOwner',
        "//table[@id='owners']//a[normalize-space(.)='PartialOne " + firstLastName + "']"), 10)
    WebUI.verifyElementPresent(xpath('secondOwner',
        "//table[@id='owners']//a[normalize-space(.)='PartialTwo " + secondLastName + "']"), 10)
} finally {
    WebUI.closeBrowser()
}
