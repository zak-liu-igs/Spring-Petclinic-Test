import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
List<List<String>> pets = [
    ['ActionA' + suffix, 'bird'],
    ['ActionB' + suffix, 'dog'],
    ['ActionC' + suffix, 'snake']
]

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'UniqueActions')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '265 Action Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    pets.each { List<String> pet ->
        WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
        WebUI.setText(findTestObject(repository + 'input_PetName'), pet[0])
        WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
        WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), pet[1], false)
        WebUI.click(findTestObject(repository + 'button_Add Pet'))
    }

    List<String> editUrls = (List<String>) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll(\"a[href*='/pets/'][href\$='/edit']\"))" +
        ".map(function(a){return a.href;});", null)
    List<String> visitUrls = (List<String>) WebUI.executeJavaScript(
        "return Array.from(document.querySelectorAll(\"a[href*='/pets/'][href\$='/visits/new']\"))" +
        ".map(function(a){return a.href;});", null)
    WebUI.verifyEqual(editUrls.size(), 3)
    WebUI.verifyEqual(visitUrls.size(), 3)

    List<String> editPetIds = editUrls.collect { String url ->
        def matcher = url =~ /\/owners\/(\d+)\/pets\/(\d+)\/edit$/
        WebUI.verifyEqual(matcher.find(), true)
        WebUI.verifyEqual(matcher.group(1), ownerId)
        return matcher.group(2)
    }
    List<String> visitPetIds = visitUrls.collect { String url ->
        def matcher = url =~ /\/owners\/(\d+)\/pets\/(\d+)\/visits\/new$/
        WebUI.verifyEqual(matcher.find(), true)
        WebUI.verifyEqual(matcher.group(1), ownerId)
        return matcher.group(2)
    }
    WebUI.verifyEqual(editPetIds.toSet().size(), 3)
    WebUI.verifyEqual(visitPetIds.toSet(), editPetIds.toSet())
} finally {
    WebUI.closeBrowser()
}
