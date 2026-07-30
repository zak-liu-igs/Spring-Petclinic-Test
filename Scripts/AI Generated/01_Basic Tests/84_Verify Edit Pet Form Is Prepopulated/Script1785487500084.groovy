import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

String stamp = System.currentTimeMillis().toString()
String firstName = 'EditFormF' + stamp
String lastName = 'EditFormL' + stamp
String address = stamp + ' Edit Pet Form Street'
String city = 'EditPetFormCity'
String telephone = '6' + stamp.substring(stamp.length() - 9)
String ownerFullName = firstName + ' ' + lastName
String petName = 'PrepopPet' + stamp
String petType = 'hamster'

String petRowXPath = "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]//tr[" +
    "td[1]//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "']]"

TestObject scopedEditPetLink = new TestObject('scopedEditPetLink')
scopedEditPetLink.addProperty('xpath', ConditionType.EQUALS,
    '(' + petRowXPath + ")[1]/td[2]//a[normalize-space(.)='Edit Pet']")

TestObject editPetHeading = new TestObject('editPetHeading')
editPetHeading.addProperty('xpath', ConditionType.EQUALS, "//h2[normalize-space(.)='Pet']")

TestObject displayedOwner = new TestObject('displayedOwner')
displayedOwner.addProperty('xpath', ConditionType.EQUALS,
    "//form//label[normalize-space(.)='Owner']/following-sibling::div[1]/span[normalize-space(.)='" +
    ownerFullName + "']")

TestObject updatePetButton = new TestObject('updatePetButton')
updatePetButton.addProperty('xpath', ConditionType.EQUALS,
    "//form//button[@type='submit' and normalize-space(.)='Update Pet']")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

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

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add New Pet'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'), 10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'), petName)

    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)

    WebUI.selectOptionByLabel(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), petType, false)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Pet'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(scopedEditPetLink, 10)

    WebUI.click(scopedEditPetLink)

    WebUI.waitForElementVisible(editPetHeading, 10)

    WebUI.verifyElementVisible(displayedOwner)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'),
        'value', petName, 10)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Birth Date'),
        'value', '2024-01-01', 10)

    String selectedPetType = WebUI.executeJavaScript("""
var s = document.getElementById('type');
if (!s) {
    throw new Error('Pet Type field was not found');
}
return s.options[s.selectedIndex].text.trim();
""", null)

    WebUI.verifyEqual(selectedPetType, petType)

    WebUI.verifyElementVisible(updatePetButton)
} finally {
    WebUI.closeBrowser()
}
