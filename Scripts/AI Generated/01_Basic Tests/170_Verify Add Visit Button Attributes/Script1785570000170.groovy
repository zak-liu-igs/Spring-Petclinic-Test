import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[contains(concat(' ', normalize-space(@class), ' '), ' form-horizontal ')]//button[normalize-space(.)='Add Visit']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(addVisitButton, 10)

    WebUI.verifyElementText(addVisitButton, 'Add Visit')
    WebUI.verifyElementAttributeValue(addVisitButton, 'type', 'submit', 10)
    WebUI.verifyMatch(WebUI.getAttribute(addVisitButton, 'class'), '.*\\bbtn\\b.*', true)
    WebUI.verifyMatch(WebUI.getAttribute(addVisitButton, 'class'), '.*\\bbtn-primary\\b.*', true)
    WebUI.verifyElementClickable(addVisitButton)
} finally {
    WebUI.closeBrowser()
}
