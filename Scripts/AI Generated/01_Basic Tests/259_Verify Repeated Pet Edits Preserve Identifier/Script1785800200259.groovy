import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String originalName = 'StableIdA' + token.substring(token.length() - 6)
String updatedName = 'StableIdB' + token.substring(token.length() - 6)
String birthDate = '2024-01-01'

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

def getEditRouteMatcher = { ->
    String currentUrl = WebUI.getUrl()
    def matcher = currentUrl =~ /\/owners\/(\d+)\/pets\/(\d+)\/edit(?:[;\/?#].*)?$/
    WebUI.verifyEqual(matcher.find(), true)
    return matcher
}

TestObject hiddenPetId = xpath('stable hidden pet id', "//form//input[@name='id' and @type='hidden']")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'StableId')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '259 Stable Id Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(buttonByText('Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerId = getOwnerIdFromCurrentUrl()

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
    WebUI.waitForPageLoad(10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), originalName)
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [birthDate])
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'cat', false)
    WebUI.click(buttonByText('Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)

    WebUI.verifyElementPresent(xpath('created stable id pet',
        "//dl[.//dd[normalize-space(.)='" + originalName + "']]" +
        "[.//dd[normalize-space(.)='" + birthDate + "']][.//dd[normalize-space(.)='cat']]"), 10)

    WebUI.click(xpath('first edit',
        "//tr[.//dl[.//dd[normalize-space(.)='" + originalName + "']]]//a[normalize-space(.)='Edit Pet']"))
    WebUI.waitForPageLoad(10)
    def firstRoute = getEditRouteMatcher()
    WebUI.verifyEqual(firstRoute.group(1), ownerId)
    String petId = firstRoute.group(2)
    WebUI.verifyElementPresent(hiddenPetId, 10)
    WebUI.verifyEqual(WebUI.getAttribute(hiddenPetId, 'value').trim(), petId)

    WebUI.clearText(findTestObject(repository + 'input_PetName'))
    WebUI.setText(findTestObject(repository + 'input_PetName'), updatedName)
    WebUI.click(buttonByText('Update Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(xpath('renamed same record after first edit',
        "//dl[.//dd[normalize-space(.)='" + updatedName + "']]" +
        "[.//dd[normalize-space(.)='" + birthDate + "']][.//dd[normalize-space(.)='cat']]"), 10)

    WebUI.click(xpath('second edit',
        "//tr[.//dl[.//dd[normalize-space(.)='" + updatedName + "']]]//a[normalize-space(.)='Edit Pet']"))
    WebUI.waitForPageLoad(10)
    def secondRoute = getEditRouteMatcher()
    WebUI.verifyEqual(secondRoute.group(1), ownerId)
    WebUI.verifyEqual(secondRoute.group(2), petId)
    WebUI.verifyElementPresent(hiddenPetId, 10)
    WebUI.verifyEqual(WebUI.getAttribute(hiddenPetId, 'value').trim(), petId)

    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'snake', false)
    WebUI.click(buttonByText('Update Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(xpath('same identifier updated record',
        "//dl[.//dd[normalize-space(.)='" + updatedName + "']]" +
        "[.//dd[normalize-space(.)='" + birthDate + "']][.//dd[normalize-space(.)='snake']]"), 10)
} finally {
    WebUI.closeBrowser()
}
