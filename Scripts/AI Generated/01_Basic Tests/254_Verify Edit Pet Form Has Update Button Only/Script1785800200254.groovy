import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = 'UpdateOnly' + token.substring(token.length() - 6)

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

def buttonByText = { String text ->
    return xpath('button ' + text, "//button[normalize-space(.)='" + text + "'] | //input[(@type='submit' or @type='button') and @value='" + text + "']")
}

def getOwnerIdFromCurrentUrl = { ->
    String currentUrl = WebUI.getUrl()
    def matcher = currentUrl =~ /\/owners\/(\d+)(?:[;\/?#].*)?$/
    WebUI.verifyEqual(matcher.find(), true)
    return matcher.group(1)
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'UpdateOnly')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '254 Update Only Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(buttonByText('Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerId = getOwnerIdFromCurrentUrl()

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
    WebUI.waitForPageLoad(10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), petName)
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'hamster', false)
    WebUI.click(buttonByText('Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)

    WebUI.verifyElementPresent(xpath('created pet details',
        "//dl[.//dd[normalize-space(.)='" + petName + "']]" +
        "[.//dd[normalize-space(.)='2024-01-01']][.//dd[normalize-space(.)='hamster']]"), 10)

    WebUI.click(xpath('edit created pet',
        "//tr[.//dl[.//dd[normalize-space(.)='" + petName + "']]]//a[normalize-space(.)='Edit Pet']"))
    WebUI.waitForPageLoad(10)

    Number updateButtons = (Number) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('form button, form input[type=submit], form input[type=button]'))" +
        ".filter(function(b){return (b.textContent || b.value || '').trim()==='Update Pet';}).length;", null)
    Number addButtons = (Number) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('form button, form input[type=submit], form input[type=button]'))" +
        ".filter(function(b){return (b.textContent || b.value || '').trim()==='Add Pet';}).length;", null)
    Number submitControls = (Number) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('form button, form input[type=submit]'))" +
        ".filter(function(b){var t=(b.getAttribute('type') || 'submit').toLowerCase(); return t==='submit';}).length;", null)

    WebUI.verifyEqual(updateButtons.intValue(), 1)
    WebUI.verifyEqual(addButtons.intValue(), 0)
    WebUI.verifyEqual(submitControls.intValue(), 1)
    WebUI.verifyTextPresent('Pet', false)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '/pets/\\d+/edit(?:[;/?#].*)?$', true)
} finally {
    WebUI.closeBrowser()
}
