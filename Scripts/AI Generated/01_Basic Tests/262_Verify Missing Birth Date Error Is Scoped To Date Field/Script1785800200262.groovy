import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = 'MissingDate' + token.substring(token.length() - 6)

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'DateScope')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '262 Date Scope Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)
    String newPetUrl = baseUrl + '/owners/' + ownerId + '/pets/new'

    WebUI.navigateToUrl(newPetUrl)
    WebUI.setText(findTestObject(repository + 'input_PetName'), petName)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'snake', false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))

    TestObject dateGroup = xpath('birth date validation group',
        "//input[@id='birthDate']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]")
    TestObject dateError = xpath('birth date required message',
        "//input[@id='birthDate']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]/span[" +
        "contains(concat(' ', normalize-space(@class), ' '), ' help-inline ')]")
    TestObject nameGroup = xpath('valid name group',
        "//input[@id='name']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]")
    WebUI.verifyMatch(WebUI.getUrl(), newPetUrl + '/?', true)
    WebUI.verifyMatch(WebUI.getAttribute(dateGroup, 'class'), '.*\\bhas-error\\b.*', true)
    WebUI.verifyElementText(dateError, 'is required')
    WebUI.verifyEqual(WebUI.getAttribute(nameGroup, 'class').contains('has-error'), false)
    WebUI.verifyEqual(WebUI.getAttribute(findTestObject(repository + 'input_PetName'), 'value'), petName)
    String selectedType = (String) WebUI.executeJavaScript(
        "var s=document.getElementById('type'); return s.options[s.selectedIndex].textContent.trim();", null)
    WebUI.verifyEqual(selectedType, 'snake')
} finally {
    WebUI.closeBrowser()
}
