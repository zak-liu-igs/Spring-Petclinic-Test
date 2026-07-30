import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String numericDescription = System.currentTimeMillis().toString()

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='description' and @name='description']")

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

TestObject savedDescription = new TestObject('savedDescription')
savedDescription.addProperty('xpath', ConditionType.EQUALS,
    "//table[contains(concat(' ', normalize-space(@class), ' '), ' table-condensed ')]//td[normalize-space(.)='" + numericDescription + "']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(descriptionInput, 10)

    WebUI.verifyMatch(numericDescription, '\\d{13}', true)
    WebUI.setText(descriptionInput, numericDescription)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), 'http://localhost:8080/owners/6')
    WebUI.waitForElementVisible(savedDescription, 10)
    WebUI.verifyElementText(savedDescription, numericDescription)
} finally {
    WebUI.closeBrowser()
}
