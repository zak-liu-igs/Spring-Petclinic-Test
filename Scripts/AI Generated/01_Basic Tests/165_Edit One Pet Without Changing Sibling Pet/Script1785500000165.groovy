import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String repository = 'Page_PetClinic  a Spring Framework demonstration/'
String token = System.currentTimeMillis().toString()
String suffix = token.substring(token.length() - 6)
String firstPet = 'Cobalt' + suffix
String renamedPet = 'Azure' + suffix
String siblingPet = 'Saffron' + suffix

def xpath = { String description, String selector ->
    TestObject object = new TestObject(description)
    object.addProperty('xpath', ConditionType.EQUALS, selector)
    return object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.setText(findTestObject(repository + 'input_First Name'), 'SiblingPet')
    WebUI.setText(findTestObject(repository + 'input_lastName'), 'Owner' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject(repository + 'input_Address'), '165 Sibling Avenue')
    WebUI.setText(findTestObject(repository + 'input_City'), 'Taipei')
    WebUI.setText(findTestObject(repository + 'input_Telephone'), token.substring(token.length() - 10))
    WebUI.click(findTestObject(repository + 'button_Add Owner'))
    def ownerMatcher = WebUI.getUrl() =~ /\/owners\/(\d+)/
    WebUI.verifyEqual(ownerMatcher.find(), true)
    String ownerId = ownerMatcher.group(1)

    [[firstPet, 'cat'], [siblingPet, 'dog']].each { List<String> pet ->
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

    TestObject firstPetEdit = xpath('first pet edit action',
        "//tr[.//dd[normalize-space(.)='" + firstPet + "']]//a[normalize-space(.)='Edit Pet']")
    WebUI.click(firstPetEdit)
    WebUI.clearText(findTestObject(repository + 'input_PetName'))
    WebUI.setText(findTestObject(repository + 'input_PetName'), renamedPet)
    WebUI.selectOptionByLabel(findTestObject(repository + 'select_Type'), 'lizard', false)
    WebUI.click(findTestObject(repository + 'button_Update Pet'))

    WebUI.verifyElementPresent(xpath('renamed first pet',
        "//dl[.//dd[normalize-space(.)='" + renamedPet + "']][.//dd[normalize-space(.)='lizard']]"), 10)
    WebUI.verifyElementPresent(xpath('unchanged sibling pet',
        "//dl[.//dd[normalize-space(.)='" + siblingPet + "']][.//dd[normalize-space(.)='dog']]"), 10)
    WebUI.verifyTextNotPresent(firstPet, false)
} finally {
    WebUI.closeBrowser()
}
