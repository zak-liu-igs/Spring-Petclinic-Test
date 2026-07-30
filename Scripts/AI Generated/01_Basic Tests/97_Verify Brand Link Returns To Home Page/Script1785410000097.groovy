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

TestObject brandLink = new TestObject('brandLink')
brandLink.addProperty('xpath', ConditionType.EQUALS,
    "//nav[@role='navigation']//a[@href='/' and contains(concat(' ', normalize-space(@class), ' '), ' navbar-brand ')]")

TestObject veterinariansHeading = new TestObject('veterinariansHeading')
veterinariansHeading.addProperty('xpath', ConditionType.EQUALS, "//h2[normalize-space(.)='Veterinarians']")

TestObject welcomeHeading = new TestObject('welcomeHeading')
welcomeHeading.addProperty('xpath', ConditionType.EQUALS, "//h2[normalize-space(.)='Welcome']")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Veterinarians'))

    WebUI.waitForElementVisible(veterinariansHeading, 10)

    WebUI.waitForElementClickable(brandLink, 10)

    WebUI.click(brandLink)

    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), 'http://localhost:8080/')

    WebUI.verifyElementVisible(welcomeHeading)
} finally {
    WebUI.closeBrowser()
}
