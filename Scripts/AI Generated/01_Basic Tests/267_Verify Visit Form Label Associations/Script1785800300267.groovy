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

    TestObject dateLabel = xpath('dateLabel', "//form[.//button[normalize-space(.)='Add Visit']]//label[normalize-space(.)='Date']")
    TestObject descriptionLabel = xpath('descriptionLabel',
        "//form[.//button[normalize-space(.)='Add Visit']]//label[normalize-space(.)='Description']")

    WebUI.verifyElementAttributeValue(dateLabel, 'for', 'date', 10)
    WebUI.verifyElementAttributeValue(descriptionLabel, 'for', 'description', 10)
    WebUI.verifyElementPresent(xpath('dateInput', "//label[@for='date']/following::input[@id='date'][1]"), 10)
    WebUI.verifyElementPresent(xpath('descriptionInput',
        "//label[@for='description']/following::input[@id='description'][1]"), 10)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('visitFormLabels',
        "//form[.//button[normalize-space(.)='Add Visit']]//label[@for]"), 10).size(), 2)
} finally {
    WebUI.closeBrowser()
}
