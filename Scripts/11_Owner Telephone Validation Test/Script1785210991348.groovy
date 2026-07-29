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
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser(null)

WebUI.navigateToUrl('http://localhost:8080/')

WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_find owners'))

WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'))

WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), 'PhoneTest')

WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Last Name'), 'Validation')

WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), '123 Test Street')

WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), 'Taipei')

WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), 'ABC123')

WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

WebUI.verifyTextPresent('Telephone must be a 10-digit number', false)

WebUI.closeBrowser()

