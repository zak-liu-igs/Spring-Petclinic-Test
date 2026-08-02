import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as Cucumber
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

String url = testData.getValue('url', 1)
String expectedTitle = testData.getValue('expected_title', 1)
String expectedHomeText = testData.getValue('expected_home_text', 1)
String expectedFindOwnersText = testData.getValue('expected_find_owners_text', 1)
String expectedVeterinariansText = testData.getValue('expected_veterinarians_text', 1)
String caseName = testData.getValue('case_name', 1)

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(url)
    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getWindowTitle(), expectedTitle, false)
    WebUI.verifyElementText(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Home'), expectedHomeText)
    WebUI.verifyElementText(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'), expectedFindOwnersText)
    WebUI.verifyElementText(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Veterinarians'), expectedVeterinariansText)

    WebUI.comment("Executed ${caseName}")
} finally {
    WebUI.closeBrowser()
}
