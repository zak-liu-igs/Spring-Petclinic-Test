import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
String birthDate = '2024-01-01'
List<String> types = ['bird', 'cat', 'dog', 'hamster', 'lizard', 'snake']
List<List<String>> pets = types.collect { String type -> [type.capitalize() + suffix, type] }

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

def countEditPetLinks = { ->
    return ((Number) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('a'))" +
        ".filter(function(a){return a.textContent.trim()==='Edit Pet' && a.getAttribute('href') && a.getAttribute('href').indexOf('/edit') >= 0;}).length;", null)).intValue()
}

def countGeneratedPetBlocks = { String generatedSuffix ->
    return ((Number) WebUI.executeJavaScript("""
var suffix = arguments[0];
return Array.from(document.querySelectorAll('dl')).filter(function(dl) {
    return Array.from(dl.querySelectorAll('dd')).some(function(dd) {
        return dd.textContent.trim().endsWith(suffix);
    });
}).length;
""", [generatedSuffix])).intValue()
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'SixTypes')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '264 Six Types Avenue')
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
        WebUI.verifyElementPresent(petRecord('pet of type ' + pet[1], pet[0], pet[1]), 10)
        WebUI.verifyEqual(countGeneratedPetBlocks(suffix), index + 1)
    }

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId)
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
    WebUI.verifyEqual(countEditPetLinks(), 6)
    WebUI.verifyEqual(countGeneratedPetBlocks(suffix), 6)
    pets.each { List<String> pet ->
        WebUI.verifyElementPresent(petRecord('pet of type ' + pet[1], pet[0], pet[1]), 10)
    }
} finally {
    WebUI.closeBrowser()
}
