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

String unknownRoute = 'route-that-does-not-exist-' + System.currentTimeMillis().toString()
String unknownUrl = 'http://localhost:8080/' + unknownRoute
String expectedDetail = 'No static resource ' + unknownRoute + '.'

TestObject errorHeading = new TestObject('errorHeading')
errorHeading.addProperty('xpath', ConditionType.EQUALS, "//h2[normalize-space(.)='Something happened...']")

TestObject notFoundMessage = new TestObject('notFoundMessage')
notFoundMessage.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Something happened...']/following-sibling::p[1]/span[" +
    "normalize-space(.)='The requested page was not found.']")

TestObject routeDetail = new TestObject('routeDetail')
routeDetail.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Something happened...']/following-sibling::p[normalize-space(.)='" +
    expectedDetail + "']")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    WebUI.navigateToUrl(unknownUrl)

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(errorHeading, 10)

    WebUI.verifyElementText(notFoundMessage, 'The requested page was not found.')

    WebUI.verifyElementText(routeDetail, expectedDetail)

    WebUI.verifyEqual(WebUI.getUrl(), unknownUrl)
} finally {
    WebUI.closeBrowser()
}
