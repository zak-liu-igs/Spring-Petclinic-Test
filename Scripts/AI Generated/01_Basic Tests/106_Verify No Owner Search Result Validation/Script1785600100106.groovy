import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String missingLastName = 'Missing' + String.valueOf(System.currentTimeMillis())

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

    TestObject feedback = xpath('notFoundFeedback',
        "//*[@id='lastName']/ancestor::div[contains(@class,'form-group')][1]//*[contains(@class,'help-inline')]")
    WebUI.verifyElementPresent(feedback, 10)
    WebUI.verifyMatch(WebUI.getText(feedback), '(?i).*has not been found.*', true)
    WebUI.verifyElementPresent(xpath('findOwnersHeading', "//h2[normalize-space(.)='Find Owners']"), 10)
} finally {
    WebUI.closeBrowser()
}
