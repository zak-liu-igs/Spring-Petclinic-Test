import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

TestObject hiddenPetId = new TestObject('hiddenPetId')
hiddenPetId.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//button[normalize-space(.)='Add Visit']]//input[@type='hidden' and @name='petId']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementPresent(hiddenPetId, 10)

    String currentUrl = WebUI.getUrl()
    WebUI.verifyMatch(currentUrl, 'http://localhost:8080/owners/\\d+/pets/\\d+/visits/new', true)

    String petIdFromUrl = currentUrl.replaceFirst('.*/pets/(\\d+)/visits/new$', '$1')
    String hiddenValue = WebUI.getAttribute(hiddenPetId, 'value')

    WebUI.verifyEqual(hiddenValue, petIdFromUrl)
    WebUI.verifyEqual(hiddenValue, '7')
} finally {
    WebUI.closeBrowser()
}
