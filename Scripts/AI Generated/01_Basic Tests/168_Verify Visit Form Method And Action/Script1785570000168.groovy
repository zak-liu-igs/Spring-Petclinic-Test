import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

TestObject visitForm = new TestObject('visitForm')
visitForm.addProperty('xpath', ConditionType.EQUALS,
    "//form[contains(concat(' ', normalize-space(@class), ' '), ' form-horizontal ') and .//button[normalize-space(.)='Add Visit']]")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(visitForm, 10)

    String effectiveMethod = WebUI.executeJavaScript(
        "return document.querySelector('form.form-horizontal').method.toLowerCase();", null)
    String effectiveAction = WebUI.executeJavaScript(
        "return document.querySelector('form.form-horizontal').action;", null)

    WebUI.verifyEqual(effectiveMethod, 'post')
    WebUI.verifyEqual(effectiveAction, visitUrl)
    WebUI.verifyElementAttributeValue(visitForm, 'method', 'post', 10)
} finally {
    WebUI.closeBrowser()
}
