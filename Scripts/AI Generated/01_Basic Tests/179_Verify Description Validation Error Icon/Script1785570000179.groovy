import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

TestObject descriptionErrorIcon = new TestObject('descriptionErrorIcon')
descriptionErrorIcon.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='description']/following-sibling::span[" +
    "contains(concat(' ', normalize-space(@class), ' '), ' fa-remove ') and " +
    "contains(concat(' ', normalize-space(@class), ' '), ' form-control-feedback ')]")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementClickable(addVisitButton, 10)

    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(descriptionErrorIcon, 10)

    WebUI.verifyElementVisible(descriptionErrorIcon)
    WebUI.verifyMatch(WebUI.getAttribute(descriptionErrorIcon, 'class'), '.*\\bfa\\b.*', true)
    WebUI.verifyMatch(WebUI.getAttribute(descriptionErrorIcon, 'class'), '.*\\bfa-remove\\b.*', true)
    WebUI.verifyElementAttributeValue(descriptionErrorIcon, 'aria-hidden', 'true', 10)
} finally {
    WebUI.closeBrowser()
}
