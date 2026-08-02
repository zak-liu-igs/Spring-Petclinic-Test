import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = '<b>Pet' + token.substring(token.length() - 6) + '</b>'
String birthDate = '2024-01-01'
String petType = 'lizard'

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

def normalizeUrl = { String url ->
    url == null ? null : url.replaceFirst(';jsessionid=[^/?#]+', '')
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

def setDateById = { String fieldId, String value ->
    WebUI.executeJavaScript("""
var d = document.getElementById(arguments[0]);
if (!d) {
    throw new Error('Date field was not found: ' + arguments[0]);
}
d.value = arguments[1];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [fieldId, value])
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_First Name'), 10)
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'EscapedPet')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '245 Escape Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    WebUI.waitForPageLoad(10)

    // PetClinic may append optional ;jsessionid=... path parameters. Normalize before extracting owner id.
    String ownerUrl = normalizeUrl(WebUI.getUrl())
    def ownerMatcher = ownerUrl =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    // Use the page's Add New Pet link from the owner details page instead of manually constructing the URL.
    WebUI.waitForElementVisible(findTestObject(repository + 'a_Add New Pet'), 10)
    WebUI.click(findTestObject(repository + 'a_Add New Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject(repository + 'input_PetName'), 10)
    WebUI.setText(findTestObject(repository + 'input_PetName'), petName)
    setDateById('birthDate', birthDate)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), petType, false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))
    WebUI.waitForPageLoad(10)

    String finalUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(finalUrl, '^' + baseUrl + '/owners/' + ownerId + '/?$', true)

    TestObject escapedLizardDetails = xpath('escaped lizard details',
        "//h2[normalize-space(.)='Pets and Visits']/following::tr[" +
        ".//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)=" + xpathLiteral(petName) + "] and " +
        ".//dt[normalize-space(.)='Birth Date']/following-sibling::dd[1][normalize-space(.)='" + birthDate + "'] and " +
        ".//dt[normalize-space(.)='Type']/following-sibling::dd[1][normalize-space(.)='" + petType + "']]")
    WebUI.verifyElementPresent(escapedLizardDetails, 10)

    Boolean isEscaped = (Boolean) WebUI.executeJavaScript("""
var expected = arguments[0];
var rows = Array.from(document.querySelectorAll('h2 + table tr, table tr'));
var row = rows.find(function (tr) {
    var nameLabel = Array.from(tr.querySelectorAll('dt')).find(function(dt) { return dt.textContent.trim() === 'Name'; });
    if (!nameLabel) {
        return false;
    }
    var dd = nameLabel.nextElementSibling;
    return dd && dd.textContent.trim() === expected;
});
if (!row) {
    return false;
}
var nameLabel = Array.from(row.querySelectorAll('dt')).find(function(dt) { return dt.textContent.trim() === 'Name'; });
var nameCell = nameLabel.nextElementSibling;
return !!nameCell &&
       nameCell.textContent.trim() === expected &&
       nameCell.querySelector('b') === null &&
       nameCell.innerHTML.indexOf('&lt;b&gt;') !== -1 &&
       nameCell.innerHTML.indexOf('&lt;/b&gt;') !== -1;
""", [petName])
    WebUI.verifyEqual(isEscaped, true)
} finally {
    WebUI.closeBrowser()
}
