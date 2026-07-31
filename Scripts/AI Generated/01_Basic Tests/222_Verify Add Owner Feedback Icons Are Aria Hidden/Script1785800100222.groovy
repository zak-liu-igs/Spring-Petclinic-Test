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

    WebUI.verifyElementPresent(xpath('firstFeedbackIcon',
        "//form[@id='add-owner-form']//*[@id='firstName']/following-sibling::span" +
        "[contains(@class,'form-control-feedback') and @aria-hidden='true']"), 10)
    WebUI.verifyEqual(WebUI.executeJavaScript("""
return String(document.querySelectorAll(
    '#add-owner-form .form-control-feedback[aria-hidden="true"]').length);
""", null), '5')
    WebUI.verifyEqual(WebUI.executeJavaScript("""
return Array.from(document.querySelectorAll('#add-owner-form .form-control-feedback'))
    .every(function(icon) {
        return icon.getAttribute('aria-hidden') === 'true' &&
            icon.classList.contains('fa') && icon.classList.contains('fa-ok');
    });
""", null), true)
} finally {
    WebUI.closeBrowser()
}
