import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)

    TestObject visitForm = xpath('visitForm', "//form[.//button[normalize-space(.)='Add Visit']]")
    WebUI.verifyElementPresent(visitForm, 10)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('editableInputs',
        "//form[.//button[normalize-space(.)='Add Visit']]//input[not(@type='hidden') and not(@disabled)]"), 10).size(), 2)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('hiddenInputs',
        "//form[.//button[normalize-space(.)='Add Visit']]//input[@type='hidden']"), 10).size(), 1)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('submitButtons',
        "//form[.//button[normalize-space(.)='Add Visit']]//button[@type='submit']"), 10).size(), 1)
    WebUI.verifyElementClickable(xpath('dateInput', "//input[@id='date' and not(@readonly)]"))
    WebUI.verifyElementClickable(xpath('descriptionInput', "//input[@id='description' and not(@readonly)]"))
} finally {
    WebUI.closeBrowser()
}
