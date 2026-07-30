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

TestObject errorNavigation = new TestObject('errorNavigation')
errorNavigation.addProperty('xpath', ConditionType.EQUALS,
    "//nav[@role='navigation']//a[@href='/oups' and .//span[normalize-space(.)='Error']]")

TestObject errorHeading = new TestObject('errorHeading')
errorHeading.addProperty('xpath', ConditionType.EQUALS, "//h2[normalize-space(.)='Something happened...']")

TestObject internalErrorMessage = new TestObject('internalErrorMessage')
internalErrorMessage.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Something happened...']/following-sibling::p[1]/span[" +
    "normalize-space(.)='An internal server error occurred.']")

TestObject exceptionDetail = new TestObject('exceptionDetail')
exceptionDetail.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Something happened...']/following-sibling::p[" +
    "normalize-space(.)='Expected: controller used to showcase what happens when an exception is thrown']")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    WebUI.click(errorNavigation)

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(errorHeading, 10)

    WebUI.verifyElementText(errorHeading, 'Something happened...')

    WebUI.verifyElementText(internalErrorMessage, 'An internal server error occurred.')

    WebUI.verifyElementText(exceptionDetail,
        'Expected: controller used to showcase what happens when an exception is thrown')

    WebUI.verifyEqual(WebUI.getUrl(), 'http://localhost:8080/oups')
} finally {
    WebUI.closeBrowser()
}
