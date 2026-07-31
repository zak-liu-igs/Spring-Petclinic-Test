import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
List<String> ownerIds = []
List<String> ownerNames = ['North' + suffix + ' OwnerA', 'South' + suffix + ' OwnerB']

try {
    WebUI.openBrowser('')
    ownerNames.eachWithIndex { String fullName, int index ->
        List<String> parts = fullName.split(' ') as List<String>
        WebUI.navigateToUrl(baseUrl + '/owners/new')
        WebUI.setText(findTestObject(repository + 'input_First Name'), parts[0])
        WebUI.setText(findTestObject(repository + 'input_lastName'), parts[1])
        WebUI.setText(findTestObject(repository + 'input_Address'), (240 + index) + ' Isolation Avenue')
        WebUI.setText(findTestObject(repository + 'input_City'), index == 0 ? 'Taipei' : 'Tainan')
        WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 9) + index)
        WebUI.click(findTestObject(repository + 'button_Add Owner'))
        def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)\/?$/
        WebUI.verifyEqual(ownerMatcher.find(), true)
        ownerIds.add(ownerMatcher.group(1))
    }

    WebUI.verifyNotEqual(ownerIds[0], ownerIds[1])
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[0] + '/pets/new')
    WebUI.verifyTextPresent(ownerNames[0], true)
    WebUI.verifyTextNotPresent(ownerNames[1], true)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerIds[0] + '/pets/new/?', true)

    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerIds[1] + '/pets/new')
    WebUI.verifyTextPresent(ownerNames[1], true)
    WebUI.verifyTextNotPresent(ownerNames[0], true)
    WebUI.verifyMatch(WebUI.getUrl(), baseUrl + '/owners/' + ownerIds[1] + '/pets/new/?', true)
} finally {
    WebUI.closeBrowser()
}
