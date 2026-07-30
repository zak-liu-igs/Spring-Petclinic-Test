import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 7)
String sharedPetName = 'Shared' + suffix

try {
    WebUI.openBrowser('')
    List<String> ownerIds = []
    [['North', 'Taipei'], ['South', 'Kaohsiung']].eachWithIndex { List<String> owner, int index ->
        WebUI.navigateToUrl(baseUrl + '/owners/new')
        WebUI.setText(findTestObject(repository + 'input_First Name'), owner[0])
        WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + suffix + index)
        WebUI.setText(findTestObject(repository + 'input_Address'), (149 + index) + ' Shared Avenue')
        WebUI.setText(findTestObject(repository + 'input_City'), owner[1])
        WebUI.setText(findTestObject(repository + 'input_Telephone'), (token.substring(token.length() - 9) + index))
        WebUI.click(findTestObject(repository + 'button_Add Owner'))
        def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)/
        WebUI.verifyEqual(ownerMatcher.find(), true)
        ownerIds.add(ownerMatcher.group(1))
    }

    ownerIds.eachWithIndex { String ownerId, int index ->
        WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
        WebUI.setText(findTestObject(repository + 'input_PetName'), sharedPetName)
        WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
        WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), index == 0 ? 'cat' : 'dog', false)
        WebUI.click(findTestObject(repository + 'button_Add Pet'))
        WebUI.verifyMatch(WebUI.getUrl(), 'http://localhost:8080/owners/' + ownerId + '/?', true)
        WebUI.verifyTextPresent(sharedPetName, false)
    }

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[0])
    WebUI.verifyTextPresent('Taipei', false)
    WebUI.verifyTextPresent(sharedPetName, false)
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[1])
    WebUI.verifyTextPresent('Kaohsiung', false)
    WebUI.verifyTextPresent(sharedPetName, false)
} finally {
    WebUI.closeBrowser()
}
