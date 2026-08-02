import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
String sharedPetName = 'Shared' + suffix
String birthDate = '2024-01-01'
List<String> ownerIds = []

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

def editPetLinkForName = { String name ->
    return xpath('edit pet ' + name,
        "//tr[.//dl[.//dd[normalize-space(.)='" + name + "']]]//a[normalize-space(.)='Edit Pet']")
}

try {
    WebUI.openBrowser('')

    ['EastEdit', 'WestKeep'].eachWithIndex { String firstName, int index ->
        WebUI.navigateToUrl(baseUrl + '/owners/new')
        WebUI.waitForPageLoad(10)
        WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
        WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + suffix + index)
        WebUI.setText(findTestObject(repository + 'input_Address'), (266 + index) + ' Shared Name Avenue')
        WebUI.setText(findTestObject(repository + 'input_City'), index == 0 ? 'Taipei' : 'Tainan')
        WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 9) + index)
        WebUI.click(buttonByText('Add Owner'))
        WebUI.waitForPageLoad(10)
        ownerIds.add(getOwnerIdFromCurrentUrl())

        WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[index] + '/pets/new')
        WebUI.waitForPageLoad(10)
        WebUI.setText(findTestObject(repository + 'input_PetName'), sharedPetName)
        setBirthDate(birthDate)
        WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), index == 0 ? 'cat' : 'dog', false)
        WebUI.click(buttonByText('Add Pet'))
        WebUI.waitForPageLoad(10)
        WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerIds[index] + '(?:[;/?#].*)?$', true)
        WebUI.verifyElementPresent(petRecord('created pet for owner ' + index, sharedPetName, index == 0 ? 'cat' : 'dog'), 10)
    }

    WebUI.verifyNotEqual(ownerIds[0], ownerIds[1])

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[0])
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerIds[0] + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(petRecord('first owner original shared pet', sharedPetName, 'cat'), 10)
    WebUI.click(editPetLinkForName(sharedPetName))
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerIds[0] + '/pets/\\d+/edit(?:[;/?#].*)?$', true)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'snake', false)
    WebUI.click(buttonByText('Update Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerIds[0] + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(petRecord('edited first owner pet', sharedPetName, 'snake'), 10)
    WebUI.verifyElementNotPresent(petRecord('stale first owner type', sharedPetName, 'cat'), 2)

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[1])
    WebUI.waitForPageLoad(10)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerIds[1] + '(?:[;/?#].*)?$', true)
    WebUI.verifyElementPresent(petRecord('unchanged second owner pet', sharedPetName, 'dog'), 10)
    WebUI.verifyElementNotPresent(petRecord('leaked first owner update', sharedPetName, 'snake'), 2)
} finally {
    WebUI.closeBrowser()
}
