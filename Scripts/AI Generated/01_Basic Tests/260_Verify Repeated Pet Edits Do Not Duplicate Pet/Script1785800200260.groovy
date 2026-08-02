import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
String birthDate = '2024-01-01'
String petType = 'dog'
List<String> names = ['EditOne' + suffix, 'EditTwo' + suffix, 'EditThree' + suffix]

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

def petRecord = { String description, String name ->
    return xpath(description,
        "//dl[.//dd[normalize-space(.)='" + name + "']]" +
        "[.//dd[normalize-space(.)='" + birthDate + "']][.//dd[normalize-space(.)='" + petType + "']]")
}

def editPetLinkForName = { String name ->
    return xpath('edit pet ' + name,
        "//tr[.//dl[.//dd[normalize-space(.)='" + name + "']]]//a[normalize-space(.)='Edit Pet']")
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'NoDuplicate')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '260 No Duplicate Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(buttonByText('Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerId = getOwnerIdFromCurrentUrl()

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
    WebUI.waitForPageLoad(10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), names[0])
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [birthDate])
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), petType, false)
    WebUI.click(buttonByText('Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(petRecord('created pet record', names[0]), 10)

    [1, 2].each { int index ->
        WebUI.click(editPetLinkForName(names[index - 1]))
        WebUI.waitForPageLoad(10)
        WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '/pets/\\d+/edit(?:[;/?#].*)?$', true)
        WebUI.clearText(findTestObject(repository + 'input_PetName'))
        WebUI.setText(findTestObject(repository + 'input_PetName'), names[index])
        WebUI.click(buttonByText('Update Pet'))
        WebUI.waitForPageLoad(10)
        WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
        WebUI.verifyElementPresent(petRecord('renamed pet record ' + index, names[index]), 10)
        WebUI.verifyTextNotPresent(names[index - 1], true)
    }

    Number editActionCount = (Number) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll('a'))" +
        ".filter(function(a){return a.textContent.trim()==='Edit Pet' && a.getAttribute('href') && a.getAttribute('href').indexOf('/pets/') >= 0 && a.getAttribute('href').indexOf('/edit') >= 0;}).length;", null)
    Number matchingPetRecordCount = (Number) WebUI.executeJavaScript("""
var expectedName = arguments[0];
return Array.from(document.querySelectorAll('dl')).filter(function(dl) {
    var dds = Array.from(dl.querySelectorAll('dd')).map(function(dd) { return dd.textContent.trim(); });
    return dds.indexOf(expectedName) >= 0;
}).length;
""", [names[2]])

    WebUI.verifyEqual(editActionCount.intValue(), 1)
    WebUI.verifyEqual(matchingPetRecordCount.intValue(), 1)
    WebUI.verifyElementPresent(petRecord('single latest pet record', names[2]), 10)
    WebUI.verifyTextNotPresent(names[0], true)
    WebUI.verifyTextNotPresent(names[1], true)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerId + '(?:[;/?#].*)?$', true)
} finally {
    WebUI.closeBrowser()
}
