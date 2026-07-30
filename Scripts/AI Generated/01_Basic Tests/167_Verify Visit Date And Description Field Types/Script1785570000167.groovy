import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

TestObject dateInput = new TestObject('dateInput')
dateInput.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//button[normalize-space(.)='Add Visit']]//input[@id='date']")

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//button[normalize-space(.)='Add Visit']]//input[@id='description']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(dateInput, 10)
    WebUI.waitForElementVisible(descriptionInput, 10)

    WebUI.verifyElementAttributeValue(dateInput, 'type', 'date', 10)
    WebUI.verifyElementAttributeValue(dateInput, 'name', 'date', 10)
    WebUI.verifyElementAttributeValue(descriptionInput, 'type', 'text', 10)
    WebUI.verifyElementAttributeValue(descriptionInput, 'name', 'description', 10)
} finally {
    WebUI.closeBrowser()
}
