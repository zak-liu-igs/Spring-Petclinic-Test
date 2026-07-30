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

TestObject welcomeHeading = new TestObject('welcomeHeading')
welcomeHeading.addProperty('xpath', ConditionType.EQUALS, "//h2[normalize-space(.)='Welcome']")

TestObject petImage = new TestObject('petImage')
petImage.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Welcome']/following::img[contains(@src, '/resources/images/pets.png')][1]")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(welcomeHeading, 10)

    WebUI.verifyElementVisible(welcomeHeading)

    WebUI.waitForElementVisible(petImage, 10)

    WebUI.verifyElementVisible(petImage)

    WebUI.verifyMatch(WebUI.getAttribute(petImage, 'src'), '.*/resources/images/pets\\.png', true)
} finally {
    WebUI.closeBrowser()
}
