import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='description' and @name='description']")

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

TestObject descriptionErrorMessage = new TestObject('descriptionErrorMessage')
descriptionErrorMessage.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='description']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]" +
    "//span[contains(concat(' ', normalize-space(@class), ' '), ' help-inline ') and normalize-space(.)!='']")

TestObject descriptionErrorIcon = new TestObject('descriptionErrorIcon')
descriptionErrorIcon.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='description']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]" +
    "//span[contains(concat(' ', normalize-space(@class), ' '), ' form-control-feedback ') and " +
    "(contains(concat(' ', normalize-space(@class), ' '), ' fa-remove ') or " +
    " contains(concat(' ', normalize-space(@class), ' '), ' fa-times ') or " +
    " contains(concat(' ', normalize-space(@class), ' '), ' glyphicon-remove '))]")

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(descriptionInput, 10)
    WebUI.waitForElementClickable(addVisitButton, 10)

    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)

    // Validation should keep the user on the Add Visit form. PetClinic may append ;jsessionid=..., so normalize first.
    String normalizedUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(normalizedUrl, '^' + visitUrl + '/?$', true)

    // Ensure the description validation fired, then validate the visual feedback icon.
    WebUI.waitForElementVisible(descriptionErrorMessage, 10)
    WebUI.verifyMatch(WebUI.getText(descriptionErrorMessage).trim(), '.*(must not be blank|must not be empty|required).*', true)

    WebUI.waitForElementVisible(descriptionErrorIcon, 10)
    WebUI.verifyElementVisible(descriptionErrorIcon)

    String iconClass = WebUI.getAttribute(descriptionErrorIcon, 'class')
    WebUI.verifyMatch(iconClass, '.*\\b(form-control-feedback)\\b.*', true)
    WebUI.verifyMatch(iconClass, '.*\\b(fa-remove|fa-times|glyphicon-remove)\\b.*', true)
    WebUI.verifyElementAttributeValue(descriptionErrorIcon, 'aria-hidden', 'true', 10)
} finally {
    WebUI.closeBrowser()
}
