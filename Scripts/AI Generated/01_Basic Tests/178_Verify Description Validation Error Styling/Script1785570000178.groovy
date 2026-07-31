import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='description' and @name='description']")

TestObject descriptionGroup = new TestObject('descriptionGroup')
descriptionGroup.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='description']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]")

TestObject descriptionError = new TestObject('descriptionError')
descriptionError.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='description']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]" +
    "//span[contains(concat(' ', normalize-space(@class), ' '), ' help-inline ') and normalize-space(.)!='']")

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
    WebUI.waitForElementVisible(descriptionError, 10)

    // Validation should keep the user on the Add Visit form. PetClinic may append ;jsessionid=...
    // to the URL, so normalize before comparing the stable route.
    String normalizedUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(normalizedUrl, '^' + visitUrl + '/?$', true)

    String groupClass = WebUI.getAttribute(descriptionGroup, 'class')
    String inputClass = WebUI.getAttribute(descriptionInput, 'class')
    String errorClass = WebUI.getAttribute(descriptionError, 'class')

    // Spring PetClinic versions may apply the error state to the form group, input, or help text.
    // Verify the field is styled as invalid without depending on only one Bootstrap-era class.
    String combinedClasses = [groupClass, inputClass, errorClass].findAll { it != null }.join(' ')
    WebUI.verifyMatch(combinedClasses, '.*\\b(has-error|is-invalid|invalid-feedback|help-inline)\\b.*', true)
    WebUI.verifyMatch(WebUI.getText(descriptionError).trim(), '.*(must not be blank|must not be empty|required).*', true)
} finally {
    WebUI.closeBrowser()
}
