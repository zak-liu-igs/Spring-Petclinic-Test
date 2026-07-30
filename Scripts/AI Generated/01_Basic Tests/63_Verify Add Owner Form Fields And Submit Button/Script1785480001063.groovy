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

TestObject ownerHeading = new TestObject('ownerHeading')
ownerHeading.addProperty('xpath', ConditionType.EQUALS, "//h2[normalize-space(.)='Owner']")

TestObject addOwnerButton = new TestObject('addOwnerButton')
addOwnerButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[@id='add-owner-form']//button[@type='submit' and normalize-space(.)='Add Owner']")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'), 10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'))

    WebUI.waitForPageLoad(10)

    WebUI.verifyElementVisible(ownerHeading)

    WebUI.verifyElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'))

    WebUI.verifyElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'))

    WebUI.verifyElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'))

    WebUI.verifyElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'))

    WebUI.verifyElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'))

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'),
        'value', '', 10)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'),
        'value', '', 10)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'),
        'value', '', 10)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'),
        'value', '', 10)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        'value', '', 10)

    WebUI.verifyElementVisible(addOwnerButton)
} finally {
    WebUI.closeBrowser()
}
