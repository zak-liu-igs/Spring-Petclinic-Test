import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl)

    String navigationLabels = WebUI.executeJavaScript("""
return Array.from(document.querySelectorAll('#main-navbar a.nav-link'))
    .map(function(link) {
        var labels = link.querySelectorAll('span');
        return labels[labels.length - 1].textContent.trim();
    })
    .join('|');
""", null)
    String navigationCount = WebUI.executeJavaScript(
        "return String(document.querySelectorAll('#main-navbar a.nav-link').length);", null)

    WebUI.verifyEqual(navigationLabels, 'Home|Find Owners|Veterinarians|Error')
    WebUI.verifyEqual(navigationCount, '4')
} finally {
    WebUI.closeBrowser()
}
