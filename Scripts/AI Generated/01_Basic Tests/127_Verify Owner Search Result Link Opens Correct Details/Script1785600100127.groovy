import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())
String sharedPrefix = 'Select' + token.substring(token.length() - 7)
String firstLastName = sharedPrefix + 'One'
String secondLastName = sharedPrefix + 'Two'
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
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), telephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
}

try {
    WebUI.openBrowser('')
    createOwner('ChoiceOne', firstLastName, '127 First Choice Road')
    createOwner('ChoiceTwo', secondLastName, '127 Second Choice Road')

    WebUI.navigateToUrl(baseUrl + '/owners/find')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), sharedPrefix)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Find Owner'))
    WebUI.click(xpath('secondOwnerLink',
        "//table[@id='owners']//a[normalize-space(.)='ChoiceTwo " + secondLastName + "']"))

    WebUI.verifyElementText(xpath('ownerName',
        "//table[contains(@class,'table-striped')]//th[normalize-space(.)='Name']/following-sibling::td"),
        'ChoiceTwo ' + secondLastName)
    WebUI.verifyElementText(xpath('ownerAddress',
        "//table[contains(@class,'table-striped')]//th[normalize-space(.)='Address']/following-sibling::td"),
        '127 Second Choice Road')
} finally {
    WebUI.closeBrowser()
}
