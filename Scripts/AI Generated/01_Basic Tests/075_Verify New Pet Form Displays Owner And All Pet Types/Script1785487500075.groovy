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
String firstName = 'PetFormF' + stamp
String lastName = 'PetFormL' + stamp
String address = stamp + ' Pet Form Street'
String city = 'PetFormCity'
String telephone = '5' + stamp.substring(stamp.length() - 9)
String ownerFullName = firstName + ' ' + lastName

TestObject newPetHeading = new TestObject('newPetHeading')
newPetHeading.addProperty('xpath', ConditionType.EQUALS, "//h2[normalize-space(.)='New Pet']")

TestObject displayedOwner = new TestObject('displayedOwner')
displayedOwner.addProperty('xpath', ConditionType.EQUALS,
    "//form//label[normalize-space(.)='Owner']/following-sibling::div[1]/span[normalize-space(.)='" +
    ownerFullName + "']")

TestObject petNameField = new TestObject('petNameField')
petNameField.addProperty('xpath', ConditionType.EQUALS,
    "//form//label[@for='name' and normalize-space(.)='Name']/following-sibling::div[1]//input[@id='name' and @name='name' and @type='text']")

TestObject birthDateField = new TestObject('birthDateField')
birthDateField.addProperty('xpath', ConditionType.EQUALS,
    "//form//label[@for='birthDate' and normalize-space(.)='Birth Date']/following-sibling::div[1]//input[@id='birthDate' and @name='birthDate' and @type='date']")

TestObject petTypeField = new TestObject('petTypeField')
petTypeField.addProperty('xpath', ConditionType.EQUALS,
    "//form//label[@for='type' and normalize-space(.)='Type']/following-sibling::div[1]//select[@id='type' and @name='type']")

TestObject petTypeOptions = new TestObject('petTypeOptions')
petTypeOptions.addProperty('xpath', ConditionType.EQUALS, "//form//select[@id='type' and @name='type']/option")

TestObject addPetButton = new TestObject('addPetButton')
addPetButton.addProperty('xpath', ConditionType.EQUALS,
    "//form//button[@type='submit' and normalize-space(.)='Add Pet']")

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

    WebUI.verifyTextPresent(ownerFullName, false)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add New Pet'))

    WebUI.waitForElementVisible(newPetHeading, 10)

    WebUI.verifyElementVisible(displayedOwner)

    WebUI.verifyElementVisible(petNameField)

    WebUI.verifyElementVisible(birthDateField)

    WebUI.verifyElementVisible(petTypeField)

    WebUI.verifyElementVisible(addPetButton)

    List<String> expectedPetTypes = ['bird', 'cat', 'dog', 'hamster', 'lizard', 'snake']
    List<String> actualPetTypes = WebUI.findWebElements(petTypeOptions, 10).collect { element ->
        element.getText().trim()
    }

    WebUI.verifyEqual(actualPetTypes, expectedPetTypes)
} finally {
    WebUI.closeBrowser()
}
