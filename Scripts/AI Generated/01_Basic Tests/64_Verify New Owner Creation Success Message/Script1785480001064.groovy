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
String firstName = 'MsgF' + stamp
String lastName = 'MsgL' + stamp
String fullName = firstName + ' ' + lastName
String address = stamp + ' Message Street'
String city = 'MessageCity'
String telephone = '1' + stamp.substring(stamp.length() - 9)

TestObject creationSuccessMessage = new TestObject('creationSuccessMessage')
creationSuccessMessage.addProperty('xpath', ConditionType.EQUALS,
    "//div[@id='success-message']//span[normalize-space(.)='New Owner Created']")

TestObject savedOwnerInformation = new TestObject('savedOwnerInformation')
savedOwnerInformation.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Owner Information']/following::table[1][" +
    ".//tr[th[normalize-space(.)='Name'] and td/b[normalize-space(.)='" + fullName + "']] and " +
    ".//tr[th[normalize-space(.)='Address'] and td[normalize-space(.)='" + address + "']] and " +
    ".//tr[th[normalize-space(.)='City'] and td[normalize-space(.)='" + city + "']] and " +
    ".//tr[th[normalize-space(.)='Telephone'] and td[normalize-space(.)='" + telephone + "']]]")

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

    WebUI.waitForElementVisible(creationSuccessMessage, 10)

    WebUI.verifyElementVisible(creationSuccessMessage)

    WebUI.verifyElementVisible(savedOwnerInformation)

    WebUI.verifyMatch(WebUI.getUrl(), 'http://localhost:8080/owners/\\d+', true)
} finally {
    WebUI.closeBrowser()
}
