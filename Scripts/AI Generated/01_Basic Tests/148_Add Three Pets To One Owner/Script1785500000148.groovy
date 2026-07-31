import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
List<List<String>> pets = [['Alpha' + suffix, 'cat'], ['Bravo' + suffix, 'dog'], ['Charlie' + suffix, 'hamster']]

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.setText(findTestObject(repository + 'input_First Name'), 'ThreePets')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '148 Trio Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerUrl = normalizeUrl(WebUI.getUrl())
    def ownerMatcher = ownerUrl =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    pets.each { List<String> pet ->
        // Use the Add New Pet action from the current owner page instead of constructing a URL.
        // This avoids issues with optional ;jsessionid path parameters and relative URL behavior.
        WebUI.waitForElementVisible(findTestObject(repository + 'a_Add New Pet'), 10)
        WebUI.click(findTestObject(repository + 'a_Add New Pet'))
        WebUI.waitForPageLoad(10)

        WebUI.waitForElementVisible(findTestObject(repository + 'input_PetName'), 10)
        WebUI.setText(findTestObject(repository + 'input_PetName'), pet[0])
        WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", ['2024-01-01'])
        WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), pet[1], false)
        WebUI.click(findTestObject(repository + 'button_Add Pet'))
        WebUI.waitForPageLoad(10)

        WebUI.verifyTextPresent(pet[0], false)
        WebUI.verifyTextPresent(pet[1], false)
    }

    pets.each { List<String> pet ->
        WebUI.verifyTextPresent(pet[0], false)
        WebUI.verifyTextPresent(pet[1], false)
    }

    TestObject petRows = xpath('added pet rows',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][" +
        pets.collect { List<String> pet -> "normalize-space(.)='" + pet[0] + "'" }.join(' or ') + "]]" )
    TestObject editActionsForAddedPets = xpath('edit actions for added pets',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][" +
        pets.collect { List<String> pet -> "normalize-space(.)='" + pet[0] + "'" }.join(' or ') + "]]//a[normalize-space(.)='Edit Pet']")

    WebUI.verifyEqual(WebUI.findWebElements(petRows, 10).size(), 3)
    WebUI.verifyEqual(WebUI.findWebElements(editActionsForAddedPets, 10).size(), 3)

    List<String> editHrefs = WebUI.findWebElements(editActionsForAddedPets, 10).collect { element ->
        normalizeUrl(element.getAttribute('href'))
    }
    editHrefs.each { String href ->
        WebUI.verifyMatch(href, '^' + baseUrl + '/owners/' + ownerId + '/pets/\\d+/edit$', true)
    }
} finally {
    WebUI.closeBrowser()
}
