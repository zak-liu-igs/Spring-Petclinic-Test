import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String newPetUrl = 'http://localhost:8080/owners/6/pets/new'

TestObject hiddenPetId = new TestObject('new pet hidden id')
hiddenPetId.addProperty('css', ConditionType.EQUALS, "form input[name='id'][type='hidden']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(newPetUrl)
    WebUI.verifyMatch(WebUI.getUrl(), 'http://localhost:8080/owners/6/pets/new/?', true)
    WebUI.verifyTextPresent('New Pet', false)
    WebUI.verifyElementPresent(hiddenPetId, 10)

    Number hiddenIdCount = (Number) WebUI.executeJavaScript(
        "return document.querySelectorAll(\"form input[name='id'][type='hidden']\").length;", null)
    WebUI.verifyEqual(hiddenIdCount.intValue(), 1)
    WebUI.verifyEqual(WebUI.getAttribute(hiddenPetId, 'value'), '')

    String formAction = (String) WebUI.executeJavaScript(
        "return document.querySelector(\"form input[name='id'][type='hidden']\").form.action;", null)
    WebUI.verifyMatch(formAction, 'http://localhost:8080/owners/6/pets/new/?', true)
} finally {
    WebUI.closeBrowser()
}
