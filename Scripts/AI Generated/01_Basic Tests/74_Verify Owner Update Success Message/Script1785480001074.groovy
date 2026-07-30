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
String firstName = 'UpdateF' + stamp
String lastName = 'UpdateL' + stamp
String originalAddress = stamp + ' Original Road'
String updatedAddress = stamp + ' Updated Road'
String city = 'UpdateCity'
String telephone = '4' + stamp.substring(stamp.length() - 9)

TestObject editOwnerLink = new TestObject('editOwnerLink')
editOwnerLink.addProperty('xpath', ConditionType.EQUALS,
    "//a[normalize-space(.)='Edit Owner' and contains(@href, '/edit')]")

TestObject updateSuccessMessage = new TestObject('updateSuccessMessage')
updateSuccessMessage.addProperty('xpath', ConditionType.EQUALS,
    "//div[@id='success-message']//span[normalize-space(.)='Owner Values Updated']")

TestObject updatedAddressRow = new TestObject('updatedAddressRow')
updatedAddressRow.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Owner Information']/following::table[1]//tr[" +
    "th[normalize-space(.)='Address'] and td[normalize-space(.)='" + updatedAddress + "']]")

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

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), originalAddress)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), city)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), telephone)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    WebUI.waitForPageLoad(10)

    WebUI.verifyTextPresent(firstName + ' ' + lastName, false)

    WebUI.click(editOwnerLink)

    WebUI.waitForPageLoad(10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), updatedAddress)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Update Owner'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(updateSuccessMessage, 10)

    WebUI.verifyElementVisible(updateSuccessMessage)

    WebUI.verifyElementVisible(updatedAddressRow)
} finally {
    WebUI.closeBrowser()
}
