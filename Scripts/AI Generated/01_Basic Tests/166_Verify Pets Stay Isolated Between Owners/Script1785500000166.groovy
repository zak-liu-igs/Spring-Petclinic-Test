import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
List<String> ownerIds = []
List<String> petNames = ['EastPet' + suffix, 'WestPet' + suffix]

try {
    WebUI.openBrowser('')
    ['EastOwner', 'WestOwner'].eachWithIndex { String firstName, int index ->
        WebUI.navigateToUrl(baseUrl + '/owners/new')
        WebUI.setText(findTestObject(repository + 'input_First Name'), firstName)
        WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + suffix + index)
        WebUI.setText(findTestObject(repository + 'input_Address'), (166 + index) + ' Isolation Avenue')
        WebUI.setText(findTestObject(repository + 'input_City'), index == 0 ? 'Taipei' : 'Tainan')
        WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 9) + index)
        WebUI.click(findTestObject(repository + 'button_Add Owner'))
        def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)/
        WebUI.verifyEqual(ownerMatcher.find(), true)
        ownerIds.add(ownerMatcher.group(1))

        WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[index] + '/pets/new')
        WebUI.setText(findTestObject(repository + 'input_PetName'), petNames[index])
        WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
        WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), index == 0 ? 'cat' : 'snake', false)
        WebUI.click(findTestObject(repository + 'button_Add Pet'))
    }

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[0])
    WebUI.verifyTextPresent(petNames[0], true)
    WebUI.verifyTextNotPresent(petNames[1], true)
    WebUI.verifyTextPresent('cat', false)
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[1])
    WebUI.verifyTextPresent(petNames[1], true)
    WebUI.verifyTextNotPresent(petNames[0], true)
    WebUI.verifyTextPresent('snake', false)
} finally {
    WebUI.closeBrowser()
}
