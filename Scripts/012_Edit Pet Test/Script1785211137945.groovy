import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String stamp = System.currentTimeMillis().toString()
String firstName = 'EditPetF' + stamp
String lastName = 'EditPetL' + stamp
String fullName = firstName + ' ' + lastName
String address = stamp + ' Edit Pet Street'
String city = 'EditPetCity'
String telephone = stamp.substring(stamp.length() - 10)
String originalPetName = 'Lucky' + stamp
String updatedPetName = 'LuckyUpdated' + stamp
String birthDate = '2024-01-01'
String petType = 'dog'

TestObject petNameInput = new TestObject('petNameInput')
petNameInput.addProperty('id', ConditionType.EQUALS, 'name')

TestObject editPetLinkForCreatedPet = new TestObject('editPetLinkForCreatedPet')
editPetLinkForCreatedPet.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + originalPetName + "']]//a[normalize-space(.)='Edit Pet']")

TestObject updatedPetNameOnOwnerDetails = new TestObject('updatedPetNameOnOwnerDetails')
updatedPetNameOnOwnerDetails.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Pets and Visits']/following::tr[.//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + updatedPetName + "']]")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl('http://localhost:8080/')
    WebUI.waitForPageLoad(10)

    // Create isolated test data instead of depending on the mutable seeded Davis/Lucky record.
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'))
    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'), 10)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), 10)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), firstName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), lastName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), address)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), city)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), telephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyTextPresent(fullName, false)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add New Pet'), 10)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add New Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(petNameInput, 10)
    WebUI.setText(petNameInput, originalPetName)

    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [birthDate])

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), 10)
    WebUI.selectOptionByLabel(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), petType, false)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyTextPresent(originalPetName, false)

    WebUI.waitForElementVisible(editPetLinkForCreatedPet, 10)
    WebUI.click(editPetLinkForCreatedPet)
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(petNameInput, 10)
    WebUI.setText(petNameInput, updatedPetName)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Update Pet'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyElementVisible(updatedPetNameOnOwnerDetails)
    WebUI.verifyTextPresent(updatedPetName, false)
    WebUI.verifyTextNotPresent(originalPetName, false)
} finally {
    WebUI.closeBrowser()
}
