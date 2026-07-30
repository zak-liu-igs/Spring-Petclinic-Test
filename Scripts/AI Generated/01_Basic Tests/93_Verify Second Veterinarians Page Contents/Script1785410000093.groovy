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

TestObject secondPageLink = new TestObject('secondPageLink')
secondPageLink.addProperty('xpath', ConditionType.EQUALS,
    "//div[.//span[normalize-space(.)='pages']]//a[normalize-space(.)='2' and contains(@href, 'page=2')]")

TestObject veterinarianRows = new TestObject('veterinarianRows')
veterinarianRows.addProperty('xpath', ConditionType.EQUALS, "//table[@id='vets']/tbody/tr")

TestObject sharonRow = new TestObject('sharonRow')
sharonRow.addProperty('xpath', ConditionType.EQUALS,
    "//table[@id='vets']/tbody/tr[td[1][normalize-space(.)='Sharon Jenkins']]")

TestObject noSpecialty = new TestObject('noSpecialty')
noSpecialty.addProperty('xpath', ConditionType.EQUALS,
    "//table[@id='vets']/tbody/tr[td[1][normalize-space(.)='Sharon Jenkins']]/td[2]/span[normalize-space(.)='none']")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Veterinarians'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(secondPageLink, 10)

    WebUI.click(secondPageLink)

    WebUI.waitForPageLoad(10)

    WebUI.verifyEqual(WebUI.getUrl(), 'http://localhost:8080/vets.html?page=2')

    WebUI.verifyEqual(WebUI.findWebElements(veterinarianRows, 10).size(), 1)

    WebUI.verifyElementVisible(sharonRow)

    WebUI.verifyElementText(noSpecialty, 'none')
} finally {
    WebUI.closeBrowser()
}
