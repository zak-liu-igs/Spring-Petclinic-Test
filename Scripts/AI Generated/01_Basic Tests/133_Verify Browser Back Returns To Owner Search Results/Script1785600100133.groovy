import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())
String sharedPrefix = 'Back' + token.substring(token.length() - 7)
String firstLastName = sharedPrefix + 'One'
String secondLastName = sharedPrefix + 'Two'
String telephone = token.substring(token.length() - 10)

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def createOwner = { String firstName, String lastName ->
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), firstName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'), lastName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '133 Browser Back Road')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), telephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
}

try {
    WebUI.openBrowser('')
    createOwner('BackOne', firstLastName)
    createOwner('BackTwo', secondLastName)

    WebUI.navigateToUrl(baseUrl + '/owners/find')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), sharedPrefix)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Find Owner'))
    WebUI.verifyElementPresent(xpath('ownersHeading', "//h2[normalize-space(.)='Owners']"), 10)

    WebUI.click(xpath('firstOwnerLink',
        "//table[@id='owners']//a[normalize-space(.)='BackOne " + firstLastName + "']"))
    WebUI.verifyElementPresent(xpath('ownerInformation', "//h2[normalize-space(.)='Owner Information']"), 10)
    WebUI.back()

    WebUI.verifyElementPresent(xpath('ownersHeadingAfterBack', "//h2[normalize-space(.)='Owners']"), 10)
    WebUI.verifyElementPresent(xpath('firstOwnerAfterBack',
        "//table[@id='owners']//a[normalize-space(.)='BackOne " + firstLastName + "']"), 10)
    WebUI.verifyElementPresent(xpath('secondOwnerAfterBack',
        "//table[@id='owners']//a[normalize-space(.)='BackTwo " + secondLastName + "']"), 10)
} finally {
    WebUI.closeBrowser()
}
