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

TestObject homeNavigationLink = new TestObject('homeNavigationLink')
homeNavigationLink.addProperty('xpath', ConditionType.EQUALS,
    "//nav//a[@href='/' and .//span[normalize-space(.)='Home']]")

TestObject findOwnersNavigationLink = new TestObject('findOwnersNavigationLink')
findOwnersNavigationLink.addProperty('xpath', ConditionType.EQUALS,
    "//nav//a[contains(@href, '/owners/find') and .//span[normalize-space(.)='Find Owners']]")

TestObject findOwnersHeading = new TestObject('findOwnersHeading')
findOwnersHeading.addProperty('xpath', ConditionType.EQUALS, "//h2[normalize-space(.)='Find Owners']")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(homeNavigationLink, 10)

    WebUI.verifyMatch(WebUI.getAttribute(homeNavigationLink, 'class'), '.*\\bactive\\b.*', true)

    WebUI.click(findOwnersNavigationLink)

    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getUrl(), 'http://localhost:8080/owners/find', false)

    WebUI.verifyElementVisible(findOwnersHeading)

    WebUI.verifyMatch(WebUI.getAttribute(findOwnersNavigationLink, 'class'), '.*\\bactive\\b.*', true)

    WebUI.verifyMatch(WebUI.getAttribute(homeNavigationLink, 'class'), '^(?!.*\\bactive\\b).*$', true)
} finally {
    WebUI.closeBrowser()
}
