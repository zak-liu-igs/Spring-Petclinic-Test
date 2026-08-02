import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = 'EditPost' + token.substring(token.length() - 6)

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

def normalizedPath = { String absoluteUrl ->
    String path = new URI(absoluteUrl).getPath()
    return path.endsWith('/') ? path.substring(0, path.length() - 1) : path
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'EditPost')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '253 Edit Route Avenue')
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
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'bird', false)
    WebUI.click(buttonByText('Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)

    WebUI.verifyElementPresent(xpath('created pet details',
        "//dl[.//dd[normalize-space(.)='" + petName + "']]" +
        "[.//dd[normalize-space(.)='2024-01-01']][.//dd[normalize-space(.)='bird']]"), 10)

    WebUI.click(xpath('edit created pet',
        "//tr[.//dl[.//dd[normalize-space(.)='" + petName + "']]]//a[normalize-space(.)='Edit Pet']"))
    WebUI.waitForPageLoad(10)

    String editUrl = WebUI.getUrl()
    def editMatcher = editUrl =~ /\/owners\/(\d+)\/pets\/(\d+)\/edit(?:[;\/?#].*)?$/
    WebUI.verifyEqual(editMatcher.find(), true)
    WebUI.verifyEqual(editMatcher.group(1), ownerId)

    String method = (String) WebUI.executeJavaScript(
        "return document.querySelector(\"form input[name='id']\").form.method.toLowerCase();", null)
    String action = (String) WebUI.executeJavaScript(
        "return document.querySelector(\"form input[name='id']\").form.action;", null)

    WebUI.verifyEqual(method, 'post')
    WebUI.verifyEqual(normalizedPath(action), normalizedPath(editUrl))
    WebUI.verifyMatch(normalizedPath(action), '/owners/' + ownerId + '/pets/' + editMatcher.group(2) + '/edit', true)
} finally {
    WebUI.closeBrowser()
}
