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

    WebUI.verifyElementPresent(xpath('dateFeedbackGroup',
        "//input[@id='date']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]" +
        "//span[contains(concat(' ', normalize-space(@class), ' '), ' form-control-feedback ') and @aria-hidden='true']"), 10)
    WebUI.verifyElementPresent(xpath('descriptionFeedbackGroup',
        "//input[@id='description']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]" +
        "//span[contains(concat(' ', normalize-space(@class), ' '), ' form-control-feedback ') and @aria-hidden='true']"), 10)
    WebUI.verifyElementAttributeValue(xpath('dateInput', "//input[@id='date']"), 'class', 'form-control', 10)
    WebUI.verifyElementAttributeValue(xpath('descriptionInput', "//input[@id='description']"), 'class', 'form-control', 10)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('feedbackIcons',
        "//form[.//button[normalize-space(.)='Add Visit']]" +
        "//span[contains(concat(' ', normalize-space(@class), ' '), ' form-control-feedback ')]"), 10).size(), 2)
} finally {
    WebUI.closeBrowser()
}
