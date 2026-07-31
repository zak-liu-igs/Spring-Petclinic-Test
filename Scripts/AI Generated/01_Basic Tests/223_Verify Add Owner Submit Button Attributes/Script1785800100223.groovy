import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')

    TestObject addOwnerButton = xpath('addOwnerButton',
        "//form[@id='add-owner-form']//button[normalize-space(.)='Add Owner']")
    WebUI.verifyElementVisible(addOwnerButton)
    WebUI.verifyElementAttributeValue(addOwnerButton, 'type', 'submit', 10)
    WebUI.verifyMatch(WebUI.getAttribute(addOwnerButton, 'class'),
        '(^|.*\\s)btn(\\s.*|$)', true)
    WebUI.verifyMatch(WebUI.getAttribute(addOwnerButton, 'class'),
        '(^|.*\\s)btn-primary(\\s.*|$)', true)
} finally {
    WebUI.closeBrowser()
}
