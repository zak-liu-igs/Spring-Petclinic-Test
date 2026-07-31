import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'BoundOwner')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '238 Binding Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))

    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')

    Number ownerControls = (Number) WebUI.executeJavaScript(
        "return document.querySelectorAll(\"form input[name='owner'],form input[name='owner.id']," +
        "form select[name='owner'],form select[name='owner.id']\").length;", null)
    Number editableIdControls = (Number) WebUI.executeJavaScript(
        "return document.querySelectorAll(\"form input[name='id']:not([type='hidden'])\").length;", null)
    Number petIdControls = (Number) WebUI.executeJavaScript(
        "return document.querySelectorAll(\"form input[name='id'][type='hidden']\").length;", null)
    WebUI.verifyEqual(ownerControls.intValue(), 0)
    WebUI.verifyEqual(editableIdControls.intValue(), 0)
    WebUI.verifyEqual(petIdControls.intValue(), 1)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '/pets/new/?', true)
} finally {
    WebUI.closeBrowser()
}
