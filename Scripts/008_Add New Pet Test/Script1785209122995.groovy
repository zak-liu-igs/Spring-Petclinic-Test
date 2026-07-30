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

WebUI.navigateToUrl('http://localhost:8080')

WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/div_col-md-12'))

WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_find owners'))

WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), 'Davis')

WebUI.sendKeys(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), Keys.chord(Keys.ENTER))

WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Test Davis'))

WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add New Pet'))

WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Name'), 'Lucky')

WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)

WebUI.selectOptionByValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), 'dog', false)

WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Pet'))

WebUI.verifyTextPresent('Lucky', false)

WebUI.closeBrowser()

