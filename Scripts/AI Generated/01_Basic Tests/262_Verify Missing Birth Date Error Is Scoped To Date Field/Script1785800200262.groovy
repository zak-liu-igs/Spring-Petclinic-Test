import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = 'MissingDate' + token.substring(token.length() - 6)
String petType = 'snake'

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

def buttonByText = { String text ->
    return xpath('button ' + text, "//button[normalize-space(.)='" + text + "'] | //input[(@type='submit' or @type='button') and @value='" + text + "']")
}

def getOwnerIdFromCurrentUrl = { ->
    String currentUrl = WebUI.getUrl()
    def matcher = currentUrl =~ /\/owners\/(\d+)(?:[;\/?#].*)?$/
    WebUI.verifyEqual(matcher.find(), true)
    return matcher.group(1)
}

TestObject dateValidationGroup = xpath('birth date validation group', "//input[@id='birthDate']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]")
TestObject dateRequiredMessage = xpath('birth date required message', "//input[@id='birthDate']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]//span[normalize-space(.)='is required']")
TestObject nameRequiredMessage = xpath('name required message', "//input[@id='name']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]//span[normalize-space(.)='is required']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'DateScope')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '262 Date Scope Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(buttonByText('Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerId = getOwnerIdFromCurrentUrl()
    String newPetUrl = baseUrl + '/owners/' + ownerId + '/pets/new'

    WebUI.navigateToUrl(newPetUrl)
    WebUI.waitForPageLoad(10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), petName)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), petType, false)
    WebUI.click(buttonByText('Add Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getUrl(), newPetUrl + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(dateValidationGroup, 10)
    WebUI.verifyElementPresent(dateRequiredMessage, 10)
    WebUI.verifyElementNotPresent(nameRequiredMessage, 2)
    WebUI.verifyEqual(WebUI.getAttribute(findTestObject(repository + 'input_PetName'), 'value'), petName)

    String selectedType = (String) WebUI.executeJavaScript(
        "var s=document.getElementById('type'); return s.options[s.selectedIndex].textContent.trim();", null)
    WebUI.verifyEqual(selectedType, petType)
} finally {
    WebUI.closeBrowser()
}
