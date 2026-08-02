import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = 'OnlyValid' + token.substring(token.length() - 6)
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

def setBirthDate = { String value ->
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [value])
}

def countEditPetLinks = { ->
    return ((Number) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('a'))" +
        ".filter(function(a){return a.textContent.trim()==='Edit Pet' && a.getAttribute('href') && a.getAttribute('href').indexOf('/pets/') >= 0 && a.getAttribute('href').indexOf('/edit') >= 0;}).length;", null)).intValue()
}

def countPetDetailBlocks = { ->
    return ((Number) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('dl')).filter(function(dl){" +
        "var text = dl.textContent || '';" +
        "return text.indexOf('Edit Pet') >= 0 || text.indexOf('Add Visit') >= 0 || dl.querySelector('dd');" +
        "}).length;", null)).intValue()
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'InvalidCount')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '263 Count Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(buttonByText('Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerId = getOwnerIdFromCurrentUrl()
    String newPetUrl = baseUrl + '/owners/' + ownerId + '/pets/new'

    WebUI.navigateToUrl(newPetUrl)
    WebUI.waitForPageLoad(10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), petName)
    setBirthDate(birthDate)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'cat', false)
    WebUI.click(buttonByText('Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)

    TestObject validPet = xpath('unchanged original pet',
        "//dl[.//dd[normalize-space(.)='" + petName + "']]" +
        "[.//dd[normalize-space(.)='" + birthDate + "']][.//dd[normalize-space(.)='cat']]")
    WebUI.verifyElementPresent(validPet, 10)
    WebUI.verifyEqual(countEditPetLinks(), 1)
    WebUI.verifyEqual(countPetDetailBlocks(), 1)

    WebUI.navigateToUrl(newPetUrl)
    WebUI.waitForPageLoad(10)
    setBirthDate(birthDate)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'dog', false)
    WebUI.click(buttonByText('Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), newPetUrl + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(xpath('missing name validation',
        "//input[@id='name']/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]//span[normalize-space(.)='is required']"), 10)

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId)
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
    WebUI.verifyEqual(countEditPetLinks(), 1)
    WebUI.verifyEqual(countPetDetailBlocks(), 1)
    WebUI.verifyElementPresent(validPet, 10)
    WebUI.verifyElementNotPresent(xpath('invalid unnamed dog record',
        "//dl[.//dd[normalize-space(.)='" + birthDate + "']][.//dd[normalize-space(.)='dog']][not(.//dd[normalize-space(.)='" + petName + "'])]"), 2)
} finally {
    WebUI.closeBrowser()
}
