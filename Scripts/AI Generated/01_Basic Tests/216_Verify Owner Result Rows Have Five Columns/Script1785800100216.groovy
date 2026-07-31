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

    WebUI.verifyElementPresent(xpath('firstOwnerRow', "//table[@id='owners']/tbody/tr[1]"), 10)
    WebUI.verifyEqual(WebUI.executeJavaScript(
        "return String(document.querySelectorAll('#owners tbody tr').length);", null), '5')
    WebUI.verifyEqual(WebUI.executeJavaScript("""
var rows = Array.from(document.querySelectorAll('#owners tbody tr'));
return rows.length > 0 && rows.every(function(row) {
    return row.querySelectorAll(':scope > td').length === 5;
});
""", null), true)
} finally {
    WebUI.closeBrowser()
}
