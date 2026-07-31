import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String originalName = 'Plain' + token.substring(token.length() - 7)
String renamedPet = "D'Arcy-" + token.substring(token.length() - 6)

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

def xpathLiteral = { String value ->
    if (!value.contains("'")) {
        return "'" + value + "'"
    }
    if (!value.contains('"')) {
        return '"' + value + '"'
    }
    return "concat('" + value.replace("'", "', \"'\", '") + "')"
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.setText(findTestObject(repository + 'input_First Name'), 'PunctEdit')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '154 Punctuation Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerUrl = normalizeUrl(WebUI.getUrl())
    def ownerMatcher = ownerUrl =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    // Use the visible Add New Pet link instead of manually constructing the URL.
    // This avoids optional ;jsessionid path parameters and route-format differences.
    WebUI.waitForElementVisible(findTestObject(repository + 'a_Add New Pet'), 10)
    WebUI.click(findTestObject(repository + 'a_Add New Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_PetName'), 10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), originalName)
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", ['2024-01-01'])
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'dog', false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyTextPresent(originalName, false)

    TestObject editPetLink = xpath('edit link for created pet',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)=" + xpathLiteral(originalName) + "]]//a[normalize-space(.)='Edit Pet']")
    WebUI.waitForElementClickable(editPetLink, 10)
    WebUI.scrollToElement(editPetLink, 5)
    WebUI.click(editPetLink)
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_PetName'), 10)
    WebUI.clearText(findTestObject(repository + 'input_PetName'))
    WebUI.setText(findTestObject(repository + 'input_PetName'), renamedPet)
    WebUI.click(findTestObject(repository + 'button_Update Pet'))
    WebUI.waitForPageLoad(10)

    TestObject renamedPetDetails = xpath('renamed pet details',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)=" + xpathLiteral(renamedPet) + "]]")
    WebUI.verifyElementPresent(renamedPetDetails, 10)
    WebUI.verifyTextPresent(renamedPet, false)
    WebUI.verifyTextNotPresent(originalName, false)

    String finalUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(finalUrl, '^' + baseUrl + '/owners/' + ownerId + '/?$', true)
} finally {
    WebUI.closeBrowser()
}
