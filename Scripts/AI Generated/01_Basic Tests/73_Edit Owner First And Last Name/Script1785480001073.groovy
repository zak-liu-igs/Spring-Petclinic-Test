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
String originalFirstName = 'OldF' + stamp
String originalLastName = 'OldL' + stamp
String originalFullName = originalFirstName + ' ' + originalLastName
String updatedFirstName = 'NewF' + stamp
String updatedLastName = 'NewL' + stamp
String updatedFullName = updatedFirstName + ' ' + updatedLastName
String address = stamp + ' Name Edit Street'
String city = 'NameEditCity'
String telephone = '3' + stamp.substring(stamp.length() - 9)

TestObject editOwnerLink = new TestObject('editOwnerLink')
editOwnerLink.addProperty('xpath', ConditionType.EQUALS,
    "//a[normalize-space(.)='Edit Owner' and contains(@href, '/edit')]")

TestObject updatedNameRow = new TestObject('updatedNameRow')
updatedNameRow.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Owner Information']/following::table[1]//tr[" +
    "th[normalize-space(.)='Name'] and td/b[normalize-space(.)='" + updatedFullName + "']]")

TestObject originalNameRow = new TestObject('originalNameRow')
originalNameRow.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Owner Information']/following::table[1]//tr[" +
    "th[normalize-space(.)='Name'] and td/b[normalize-space(.)='" + originalFullName + "']]")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'), 10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), 10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), originalFirstName)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), originalLastName)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), address)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), city)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), telephone)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    WebUI.waitForPageLoad(10)

    WebUI.verifyTextPresent(originalFullName, false)

    WebUI.click(editOwnerLink)

    WebUI.waitForPageLoad(10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), updatedFirstName)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), updatedLastName)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Update Owner'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(updatedNameRow, 10)

    WebUI.verifyElementVisible(updatedNameRow)

    WebUI.verifyElementNotPresent(originalNameRow, 1)
} finally {
    WebUI.closeBrowser()
}
