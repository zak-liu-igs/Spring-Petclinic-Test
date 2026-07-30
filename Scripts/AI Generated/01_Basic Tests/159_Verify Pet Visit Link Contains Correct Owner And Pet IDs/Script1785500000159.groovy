import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String petName = 'VisitLink' + token.substring(token.length() - 6)

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'VisitLink')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '159 Link Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)
    WebUI.navigateToUrl(baseUrl + '/owners/' + ownerId + '/pets/new')
    WebUI.setText(findTestObject(repository + 'input_PetName'), petName)
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'bird', false)
    WebUI.click(findTestObject(repository + 'button_Add Pet'))

    String editHref = WebUI.getAttribute(xpath('edit link',
        "//a[normalize-space(.)='Edit Pet' and starts-with(@href,'/owners/" + ownerId + "/pets/')]"), 'href')
    String visitHref = WebUI.getAttribute(xpath('visit link',
        "//a[normalize-space(.)='Add Visit' and starts-with(@href,'/owners/" + ownerId + "/pets/')]"), 'href')
    def editMatcher = editHref =~ /\/owners\/(\d+)\/pets\/(\d+)\/edit$/
    def visitMatcher = visitHref =~ /\/owners\/(\d+)\/pets\/(\d+)\/visits\/new$/
    WebUI.verifyEqual(editMatcher.find(), true)
    WebUI.verifyEqual(visitMatcher.find(), true)
    WebUI.verifyEqual(visitMatcher.group(1), ownerId)
    WebUI.verifyEqual(visitMatcher.group(1), editMatcher.group(1))
    WebUI.verifyEqual(visitMatcher.group(2), editMatcher.group(2))
} finally {
    WebUI.closeBrowser()
}
