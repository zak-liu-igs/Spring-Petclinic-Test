import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String stamp = System.currentTimeMillis().toString()
String upperDescription = ('CaseVariant' + stamp).toUpperCase()
String lowerDescription = upperDescription.toLowerCase()

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='description' and @name='description']")

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(descriptionInput, 10)
    WebUI.setText(descriptionInput, upperDescription)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)
    WebUI.verifyEqual(WebUI.getUrl(), 'http://localhost:8080/owners/6')
    WebUI.verifyTextPresent(upperDescription, false)

    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(descriptionInput, 10)
    WebUI.setText(descriptionInput, lowerDescription)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), 'http://localhost:8080/owners/6')
    WebUI.verifyTextPresent(upperDescription, false)
    WebUI.verifyTextPresent(lowerDescription, false)
    WebUI.verifyNotEqual(upperDescription, lowerDescription)
} finally {
    WebUI.closeBrowser()
}
