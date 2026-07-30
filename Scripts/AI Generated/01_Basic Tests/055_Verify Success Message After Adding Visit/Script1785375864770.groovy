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
String firstName = 'SuccessFirst' + stamp
String lastName = 'SuccessLast' + stamp
String address = stamp + ' Success Visit Street'
String city = 'SuccessCity'
String telephone = stamp.substring(stamp.length() - 10)
String petName = 'SuccessPet' + stamp
String visitDescription = 'Success message visit ' + stamp

TestObject addVisitLink = new TestObject('addVisitLink')
addVisitLink.addProperty('xpath', ConditionType.EQUALS,
    "//a[normalize-space(.)='Add Visit' and contains(@href, '/visits/new')]")

TestObject successMessage = new TestObject('successMessage')
successMessage.addProperty('xpath', ConditionType.EQUALS,
    "//*[@id='success-message']")

TestObject savedVisit = new TestObject('savedVisit')
savedVisit.addProperty('xpath', ConditionType.EQUALS,
    "//table[.//th[normalize-space(.)='Visit Date'] and .//th[normalize-space(.)='Description']]//td[normalize-space(.)='" +
    visitDescription + "']")

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

    WebUI.verifyTextPresent(firstName + ' ' + lastName, false)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add New Pet'), 10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add New Pet'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'), 10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'), petName)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Birth Date'), 10)

    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), 10)

    WebUI.selectOptionByLabel(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), 'dog', false)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Pet'))

    WebUI.waitForPageLoad(10)

    WebUI.verifyTextPresent(petName, false)

    WebUI.waitForElementVisible(addVisitLink, 10)

    WebUI.click(addVisitLink)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/Page_PetClinic  a Spring Framework demonstration/input_Description'),
        10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/Page_PetClinic  a Spring Framework demonstration/input_Description'),
        visitDescription)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Visit'))

    WebUI.waitForElementVisible(successMessage, 10)

    WebUI.verifyElementText(successMessage, 'Your visit has been booked')

    WebUI.verifyElementVisible(savedVisit)

    WebUI.verifyElementText(savedVisit, visitDescription)
} finally {
    WebUI.closeBrowser()
}
