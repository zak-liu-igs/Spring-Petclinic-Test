import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners?lastName=')

    WebUI.verifyElementPresent(xpath('ownersHeading', "//h2[normalize-space(.)='Owners']"), 10)
    WebUI.verifyElementPresent(xpath('ownersTable', "//table[@id='owners']"), 10)
    WebUI.verifyEqual(WebUI.executeJavaScript("""
return Array.from(document.querySelectorAll('#owners thead th'))
    .map(function(header) { return header.textContent.trim(); })
    .join('|');
""", null), 'Name|Address|City|Telephone|Pets')
    WebUI.verifyEqual(WebUI.executeJavaScript(
        "return String(document.querySelectorAll('#owners thead th').length);", null), '5')
} finally {
    WebUI.closeBrowser()
}
