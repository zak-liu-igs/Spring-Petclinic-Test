import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String firstName = 'PetForm'
String lastName = 'Owner' + token.substring(token.length() - 7)

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
    WebUI.setText(findTestObject(repository + 'input_lastName'), lastName)
    WebUI.setText(findTestObject(repository + 'input_Address'), '134 Context Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))

    String ownerUrl = WebUI.getUrl()
    def ownerMatcher = ownerUrl =~ /\/owners\/(\d+)/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
    WebUI.verifyMatch(WebUI.getUrl(), 'http://localhost:8080/owners/' + ownerId + '/pets/new/?', true)
    WebUI.verifyTextPresent('New Pet', false)
    WebUI.verifyTextPresent(firstName + ' ' + lastName, false)
    WebUI.verifyElementVisible(findTestObject(repository + 'input_PetName'))
    WebUI.verifyElementVisible(findTestObject(repository + 'input_Birth Date'))
    WebUI.verifyElementVisible(findTestObject(repository + 'select_Type'))
    WebUI.verifyElementVisible(findTestObject(repository + 'button_Add Pet'))
} finally {
    WebUI.closeBrowser()
}
