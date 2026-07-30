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
String firstName = 'DupPetF' + stamp
String lastName = 'DupPetL' + stamp
String address = stamp + ' Duplicate Pet Street'
String city = 'DuplicatePetCity'
String telephone = '9' + stamp.substring(stamp.length() - 9)
String petName = 'Duplicate' + stamp

TestObject duplicateNameError = new TestObject('duplicateNameError')
duplicateNameError.addProperty('xpath', ConditionType.EQUALS,
    "//form//label[@for='name' and normalize-space(.)='Name']/following-sibling::div[1]" +
    "//span[contains(concat(' ', normalize-space(@class), ' '), ' help-inline ') and normalize-space(.)='is already in use']")

TestObject newPetHeading = new TestObject('newPetHeading')
newPetHeading.addProperty('xpath', ConditionType.EQUALS, "//h2[normalize-space(.)='New Pet']")

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

    WebUI.selectOptionByLabel(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), 'dog', false)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Pet'))

    WebUI.waitForPageLoad(10)

    WebUI.verifyTextPresent(petName, false)

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

    WebUI.selectOptionByLabel(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), 'cat', false)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Pet'))

    WebUI.waitForElementVisible(duplicateNameError, 10)

    WebUI.verifyElementVisible(duplicateNameError)

    WebUI.verifyElementVisible(newPetHeading)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'),
        'value', petName, 10)
} finally {
    WebUI.closeBrowser()
}
