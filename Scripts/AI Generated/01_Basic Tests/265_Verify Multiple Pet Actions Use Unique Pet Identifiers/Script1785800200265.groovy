import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
String birthDate = '2024-01-01'
List<List<String>> pets = [
    ['ActionA' + suffix, 'bird'],
    ['ActionB' + suffix, 'dog'],
    ['ActionC' + suffix, 'snake']
]

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

def petRecord = { String description, String name, String type ->
    return xpath(description,
        "//dl[.//dd[normalize-space(.)='" + name + "']]" +
        "[.//dd[normalize-space(.)='" + birthDate + "']][.//dd[normalize-space(.)='" + type + "']]")
}

def getActionUrlsForGeneratedPets = { String linkText ->
    return (List<String>) WebUI.executeJavaScript("""
var suffix = arguments[0];
var linkText = arguments[1];
return Array.from(document.querySelectorAll('tr')).filter(function(row) {
    return Array.from(row.querySelectorAll('dd')).some(function(dd) {
        return dd.textContent.trim().endsWith(suffix);
    });
}).map(function(row) {
    var link = Array.from(row.querySelectorAll('a')).find(function(a) {
        return a.textContent.trim() === linkText;
    });
    return link ? link.href : null;
}).filter(function(url) { return !!url; });
""", [suffix, linkText])
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'UniqueActions')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '265 Action Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(buttonByText('Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerId = getOwnerIdFromCurrentUrl()

    pets.eachWithIndex { List<String> pet, int index ->
        WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
        WebUI.waitForPageLoad(10)
        WebUI.setText(findTestObject(repository + 'input_PetName'), pet[0])
        setBirthDate(birthDate)
        WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), pet[1], false)
        WebUI.click(buttonByText('Add Pet'))
        WebUI.waitForPageLoad(10)
        WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
        WebUI.verifyElementPresent(petRecord('created pet ' + pet[0], pet[0], pet[1]), 10)
        WebUI.verifyEqual(getActionUrlsForGeneratedPets('Edit Pet').size(), index + 1)
        WebUI.verifyEqual(getActionUrlsForGeneratedPets('Add Visit').size(), index + 1)
    }

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId)
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)

    List<String> editUrls = getActionUrlsForGeneratedPets('Edit Pet')
    List<String> visitUrls = getActionUrlsForGeneratedPets('Add Visit')
    WebUI.verifyEqual(editUrls.size(), 3)
    WebUI.verifyEqual(visitUrls.size(), 3)

    List<String> editPetIds = editUrls.collect { String url ->
        def matcher = url =~ /\/owners\/(\d+)\/pets\/(\d+)\/edit(?:[;\/?#].*)?$/
        WebUI.verifyEqual(matcher.find(), true)
        WebUI.verifyEqual(matcher.group(1), ownerId)
        return matcher.group(2)
    }
    List<String> visitPetIds = visitUrls.collect { String url ->
        def matcher = url =~ /\/owners\/(\d+)\/pets\/(\d+)\/visits\/new(?:[;\/?#].*)?$/
        WebUI.verifyEqual(matcher.find(), true)
        WebUI.verifyEqual(matcher.group(1), ownerId)
        return matcher.group(2)
    }

    WebUI.verifyEqual(editPetIds.toSet().size(), 3)
    WebUI.verifyEqual(visitPetIds.toSet().size(), 3)
    WebUI.verifyEqual(visitPetIds.toSet(), editPetIds.toSet())
    pets.each { List<String> pet ->
        WebUI.verifyElementPresent(petRecord('pet action record ' + pet[0], pet[0], pet[1]), 10)
    }
} finally {
    WebUI.closeBrowser()
}
