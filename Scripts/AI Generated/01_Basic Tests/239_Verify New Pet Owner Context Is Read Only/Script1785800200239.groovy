import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String firstName = 'ReadOnly'
String lastName = 'Owner' + token.substring(token.length() - 7)
String fullName = firstName + ' ' + lastName

TestObject ownerText = new TestObject('read only owner context')
ownerText.addProperty('xpath', ConditionType.EQUALS,
    "//form//label[normalize-space(.)='Owner']/following-sibling::div[1]/span[normalize-space(.)='" +
    fullName + "']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
    WebUI.setText(findTestObject(repository + 'input_lastName'), lastName)
    WebUI.setText(findTestObject(repository + 'input_Address'), '239 Read Only Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))

    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')

    WebUI.verifyElementVisible(ownerText)
    WebUI.verifyElementText(ownerText, fullName)
    Number editableOwnerContext = (Number) WebUI.executeJavaScript(
        "var label=Array.from(document.querySelectorAll('form label')).find(function(l){" +
        "return l.textContent.trim()==='Owner';}); var group=label.parentElement;" +
        "return group.querySelectorAll('input,select,textarea,button').length;", null)
    WebUI.verifyEqual(editableOwnerContext.intValue(), 0)
} finally {
    WebUI.closeBrowser()
}
