import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()

TestObject hiddenPetId = new TestObject('new pet hidden id')
hiddenPetId.addProperty('css', ConditionType.EQUALS, "form input[name='id'][type='hidden']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'Defaults')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '234 Defaults Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))

    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')

    WebUI.verifyEqual(WebUI.getAttribute(findTestObject(repository + 'input_PetName'), 'value'), '')
    WebUI.verifyEqual(WebUI.getAttribute(findTestObject(repository + 'input_Birth Date'), 'value'), '')
    WebUI.verifyElementPresent(hiddenPetId, 10)
    WebUI.verifyEqual(WebUI.getAttribute(hiddenPetId, 'value'), '')
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '/pets/new/?', true)
} finally {
    WebUI.closeBrowser()
}
