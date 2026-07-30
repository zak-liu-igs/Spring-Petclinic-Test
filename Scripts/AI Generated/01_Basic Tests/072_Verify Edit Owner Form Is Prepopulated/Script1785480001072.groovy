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

String stamp = System.currentTimeMillis().toString()
String firstName = 'PrefillF' + stamp
String lastName = 'PrefillL' + stamp
String address = stamp + ' Prefill Street'
String city = 'PrefillCity'
String telephone = '2' + stamp.substring(stamp.length() - 9)

TestObject editOwnerLink = new TestObject('editOwnerLink')
editOwnerLink.addProperty('xpath', ConditionType.EQUALS,
    "//a[normalize-space(.)='Edit Owner' and contains(@href, '/edit')]")

TestObject updateOwnerButton = new TestObject('updateOwnerButton')
updateOwnerButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[@id='add-owner-form']//button[@type='submit' and normalize-space(.)='Update Owner']")

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'), 10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), 10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), firstName)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), lastName)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), address)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), city)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), telephone)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(editOwnerLink, 10)

    WebUI.click(editOwnerLink)

    WebUI.waitForPageLoad(10)

    WebUI.verifyMatch(WebUI.getUrl(), 'http://localhost:8080/owners/\\d+/edit', true)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'),
        'value', firstName, 10)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'),
        'value', lastName, 10)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'),
        'value', address, 10)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'),
        'value', city, 10)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'),
        'value', telephone, 10)

    WebUI.verifyElementVisible(updateOwnerButton)
} finally {
    WebUI.closeBrowser()
}
