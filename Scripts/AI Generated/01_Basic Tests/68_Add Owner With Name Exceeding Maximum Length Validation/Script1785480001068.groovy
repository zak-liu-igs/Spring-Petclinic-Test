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
String tooLongFirstName = ('TooLong' + stamp).padRight(31, 'X')
String lastName = 'LengthL' + stamp
String address = stamp + ' Length Street'
String city = 'LengthCity'
String telephone = '5' + stamp.substring(stamp.length() - 9)

TestObject firstNameLengthValidation = new TestObject('firstNameLengthValidation')
firstNameLengthValidation.addProperty('xpath', ConditionType.EQUALS,
    "//input[@id='firstName']/ancestor::div[" +
    "contains(concat(' ', normalize-space(@class), ' '), ' form-group ')][1]//span[" +
    "contains(concat(' ', normalize-space(@class), ' '), ' help-inline ') and " +
    "normalize-space(.)='size must be between 0 and 30']")

TestObject creationSuccessMessage = new TestObject('creationSuccessMessage')
creationSuccessMessage.addProperty('xpath', ConditionType.EQUALS, "//div[@id='success-message']")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(tooLongFirstName.length(), 31)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'), 10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), 10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), tooLongFirstName)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), lastName)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), address)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), city)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), telephone)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(firstNameLengthValidation, 10)

    WebUI.verifyElementVisible(firstNameLengthValidation)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'),
        'value', tooLongFirstName, 10)

    WebUI.verifyMatch(WebUI.getUrl(), 'http://localhost:8080/owners/new', false)

    WebUI.verifyElementNotPresent(creationSuccessMessage, 1)
} finally {
    WebUI.closeBrowser()
}
