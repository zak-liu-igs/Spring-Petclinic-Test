import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
String token = String.valueOf(System.currentTimeMillis())

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')
    WebUI.waitForPageLoad(10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'),
        'Actions' + token.substring(token.length() - 5))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'),
        'Links' + token.substring(token.length() - 7))
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '123 Action Street')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        token.substring(token.length() - 10))
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
    WebUI.waitForPageLoad(10)

    String ownerUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(ownerUrl, '^' + baseUrl + '/owners/\\d+/?$', true)

    String ownerId = (ownerUrl =~ /\/owners\/(\d+)\/?$/)[0][1]

    TestObject editOwner = xpath('editOwner', "//a[normalize-space(.)='Edit Owner']")
    TestObject addNewPet = xpath('addNewPet', "//a[normalize-space(.)='Add New Pet']")

    WebUI.verifyElementVisible(editOwner)
    WebUI.verifyElementVisible(addNewPet)

    String editOwnerHref = normalizeUrl(WebUI.getAttribute(editOwner, 'href'))
    String addNewPetHref = normalizeUrl(WebUI.getAttribute(addNewPet, 'href'))

    // PetClinic may include or omit ;jsessionid in URLs depending on session/cookie state.
    // Validate the stable owner id and route after removing any optional session path parameter.
    WebUI.verifyMatch(editOwnerHref, '^' + baseUrl + '/owners/' + ownerId + '/edit$', true)
    WebUI.verifyMatch(addNewPetHref, '^' + baseUrl + '/owners/' + ownerId + '/pets/new$', true)
} finally {
    WebUI.closeBrowser()
}
