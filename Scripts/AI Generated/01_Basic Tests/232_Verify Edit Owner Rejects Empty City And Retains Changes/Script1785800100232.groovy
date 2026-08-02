import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = String.valueOf(System.currentTimeMillis())
String firstName = 'EditCity' + token.substring(token.length() - 5)
String lastName = 'Required' + token.substring(token.length() - 6)
String newAddress = '232 Retained Change Road'
String newTelephone = '8' + token.substring(token.length() - 9)

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

def createOwner = {
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_First Name'), 10)
    WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
    WebUI.setText(findTestObject(repository + 'input_Last Name'), lastName)
    WebUI.setText(findTestObject(repository + 'input_Address'), '232 Original Road')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Original City')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)
}

try {
    WebUI.openBrowser('')
    createOwner()

    // PetClinic may append optional ;jsessionid=... path parameters. Normalize before extracting owner id.
    String ownerUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(ownerUrl, '^' + baseUrl + '/owners/\\d+/?$', true)
    String ownerId = (ownerUrl =~ /\/owners\/(\d+)\/?$/)[0][1]

    TestObject editOwner = xpath('editOwner', "//a[normalize-space(.)='Edit Owner']")
    WebUI.waitForElementClickable(editOwner, 10)
    WebUI.click(editOwner)
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_Address'), 10)
    WebUI.setText(findTestObject(repository + 'input_Address'), newAddress)
    WebUI.clearText(findTestObject(repository + 'input_City'))
    WebUI.setText(findTestObject(repository + 'input_Telephone'), newTelephone)
    WebUI.click(findTestObject(repository + 'button_Update Owner'))
    WebUI.waitForPageLoad(10)

    TestObject cityError = xpath('cityError',
        "//*[@id='city']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]" +
        "//*[self::span or self::div][normalize-space(.)!='' and " +
        "(contains(concat(' ', normalize-space(@class), ' '), ' help-inline ') or " +
        " contains(concat(' ', normalize-space(@class), ' '), ' invalid-feedback ') or " +
        " contains(concat(' ', normalize-space(@class), ' '), ' text-danger '))]")

    WebUI.verifyElementPresent(cityError, 10)
    WebUI.verifyMatch(WebUI.getText(cityError).trim(), '.*(must not be blank|must not be empty|required).*', true)

    WebUI.verifyElementAttributeValue(findTestObject(repository + 'input_Address'), 'value', newAddress, 10)
    WebUI.verifyElementAttributeValue(findTestObject(repository + 'input_City'), 'value', '', 10)
    WebUI.verifyElementAttributeValue(findTestObject(repository + 'input_Telephone'), 'value', newTelephone, 10)

    String editUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(editUrl, '^' + baseUrl + '/owners/' + ownerId + '/edit/?$', true)
} finally {
    WebUI.closeBrowser()
}
