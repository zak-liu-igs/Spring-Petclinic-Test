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
    WebUI.waitForPageLoad(10)

    // In the current PetClinic markup, each input is wrapped in an inner <div> and the feedback icon is
    // a sibling of that wrapper inside the same .col-sm-10 container, not a direct sibling of the input.
    WebUI.verifyElementPresent(xpath('firstFeedbackIcon',
        "//form[@id='add-owner-form']//*[@id='firstName']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' col-sm-10 ')][1]" +
        "//span[contains(concat(' ', normalize-space(@class), ' '), ' form-control-feedback ') and @aria-hidden='true']"), 10)

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

    WebUI.verifyEqual(WebUI.executeJavaScript("""
var fieldIds = ['firstName', 'lastName', 'address', 'city', 'telephone'];
return fieldIds.every(function(id) {
    var input = document.getElementById(id);
    if (!input) {
        return false;
    }
    var container = input.closest('.col-sm-10');
    return !!container && !!container.querySelector('.form-control-feedback[aria-hidden="true"].fa.fa-ok');
});
""", null), true)
} finally {
    WebUI.closeBrowser()
}
