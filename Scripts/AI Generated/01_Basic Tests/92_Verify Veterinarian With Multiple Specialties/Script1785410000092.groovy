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

String lindaRowXPath = "//table[@id='vets']/tbody/tr[td[1][normalize-space(.)='Linda Douglas']]"

TestObject veterinarianRow = new TestObject('veterinarianRow')
veterinarianRow.addProperty('xpath', ConditionType.EQUALS, lindaRowXPath)

TestObject specialties = new TestObject('specialties')
specialties.addProperty('xpath', ConditionType.EQUALS, lindaRowXPath + '/td[2]/span[normalize-space(.)]')

TestObject dentistry = new TestObject('dentistry')
dentistry.addProperty('xpath', ConditionType.EQUALS,
    lindaRowXPath + "/td[2]/span[normalize-space(.)='dentistry']")

TestObject surgery = new TestObject('surgery')
surgery.addProperty('xpath', ConditionType.EQUALS,
    lindaRowXPath + "/td[2]/span[normalize-space(.)='surgery']")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Veterinarians'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(veterinarianRow, 10)

    WebUI.verifyEqual(WebUI.findWebElements(specialties, 10).size(), 2)

    WebUI.verifyElementText(dentistry, 'dentistry')

    WebUI.verifyElementText(surgery, 'surgery')
} finally {
    WebUI.closeBrowser()
}
