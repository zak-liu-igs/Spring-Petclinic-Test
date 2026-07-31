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

    WebUI.verifyElementPresent(xpath('firstOwnerLink',
        "//table[@id='owners']/tbody/tr[1]/td[1]/a"), 10)
    WebUI.verifyEqual(WebUI.executeJavaScript('''
var links = Array.from(document.querySelectorAll('#owners tbody tr td:first-child a'));
return links.length > 0 && links.every(function(link) {
    var target = new URL(link.href);
    return target.origin === 'http://localhost:8080' &&
        /^\\/owners\\/\\d+$/.test(target.pathname);
});
''', null), true)
    WebUI.verifyEqual(WebUI.executeJavaScript('''
return Array.from(document.querySelectorAll('#owners tbody tr td:first-child a'))
    .every(function(link) { return link.textContent.trim().length > 0; });
''', null), true)
} finally {
    WebUI.closeBrowser()
}
