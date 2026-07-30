import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

TestObject descriptionGroup = new TestObject('descriptionGroup')
descriptionGroup.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='description']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]")

TestObject descriptionError = new TestObject('descriptionError')
descriptionError.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='description']/following-sibling::span[contains(concat(' ', normalize-space(@class), ' '), ' help-inline ')]")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementClickable(addVisitButton, 10)

    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(descriptionError, 10)

    WebUI.verifyEqual(WebUI.getUrl(), visitUrl)
    WebUI.verifyMatch(WebUI.getAttribute(descriptionGroup, 'class'), '.*\\bhas-error\\b.*', true)
    WebUI.verifyElementText(descriptionError, 'must not be blank')
} finally {
    WebUI.closeBrowser()
}
