import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String missingLastName = 'Unlisted' + String.valueOf(System.currentTimeMillis())

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/find')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'),
        missingLastName)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Find Owner'))

    WebUI.verifyElementPresent(xpath('notFoundFeedback',
        "//*[@id='lastName']/ancestor::div[contains(@class,'form-group')][1]//*[contains(@class,'help-inline')]"), 10)
    WebUI.verifyElementAttributeValue(
        findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'),
        'value', missingLastName, 10)
} finally {
    WebUI.closeBrowser()
}
