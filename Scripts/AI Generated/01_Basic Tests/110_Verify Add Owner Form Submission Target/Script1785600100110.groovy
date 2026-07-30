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

    TestObject ownerForm = xpath('ownerForm',
        "//form[.//*[@id='firstName'] and .//button[normalize-space(.)='Add Owner']]")
    WebUI.verifyElementPresent(ownerForm, 10)
    WebUI.verifyElementAttributeValue(ownerForm, 'method', 'post', 10)
    WebUI.verifyMatch(WebUI.getAttribute(ownerForm, 'action'), '^http://localhost:8080/owners/new$', true)
} finally {
    WebUI.closeBrowser()
}
