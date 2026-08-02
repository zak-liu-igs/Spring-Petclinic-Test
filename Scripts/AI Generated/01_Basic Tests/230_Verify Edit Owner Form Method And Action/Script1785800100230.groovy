import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = String.valueOf(System.currentTimeMillis())

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
    WebUI.setText(findTestObject(repository + 'input_First Name'),
        'Method' + token.substring(token.length() - 6))
    WebUI.setText(findTestObject(repository + 'input_Last Name'),
        'Action' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '230 Method Road')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)
}

try {
    WebUI.openBrowser('')
    createOwner()

    // PetClinic may append optional ;jsessionid=... path parameters. Normalize before extracting the owner id.
    String ownerUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(ownerUrl, '^' + baseUrl + '/owners/\\d+/?$', true)
    String ownerId = (ownerUrl =~ /\/owners\/(\d+)\/?$/)[0][1]

    TestObject editOwner = xpath('editOwner', "//a[normalize-space(.)='Edit Owner']")
    WebUI.waitForElementClickable(editOwner, 10)
    WebUI.click(editOwner)
    WebUI.waitForPageLoad(10)

    TestObject editForm = xpath('editOwnerForm',
        "//form[@id='add-owner-form' and .//button[normalize-space(.)='Update Owner']]")
    WebUI.verifyElementPresent(editForm, 10)
    WebUI.verifyElementAttributeValue(editForm, 'method', 'post', 10)

    String formAction = normalizeUrl(WebUI.getAttribute(editForm, 'action'))
    WebUI.verifyMatch(formAction, '^' + baseUrl + '/owners/' + ownerId + '/edit$', true)

    String editUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(editUrl, '^' + baseUrl + '/owners/' + ownerId + '/edit/?$', true)
} finally {
    WebUI.closeBrowser()
}
