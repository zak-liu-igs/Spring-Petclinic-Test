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
String firstName = 'Assoc' + stamp
String lastName = 'VA' + stamp
String address = stamp + ' Association Street'
String city = 'AssocCity'
String telephone = stamp.substring(stamp.length() - 10)
String firstPetName = 'AssocA' + stamp
String secondPetName = 'AssocB' + stamp
String visitDescription = 'First pet association ' + stamp

String firstPetRowXPath = "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]//tr[" +
    "td[1]//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + firstPetName + "']]"
String secondPetRowXPath = "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]//tr[" +
    "td[1]//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + secondPetName + "']]"

TestObject firstPetRow = new TestObject('firstPetRow')
firstPetRow.addProperty('xpath', ConditionType.EQUALS, firstPetRowXPath)

TestObject secondPetRow = new TestObject('secondPetRow')
secondPetRow.addProperty('xpath', ConditionType.EQUALS, secondPetRowXPath)

TestObject firstPetAddVisitLink = new TestObject('firstPetAddVisitLink')
firstPetAddVisitLink.addProperty('xpath', ConditionType.EQUALS,
    '(' + firstPetRowXPath + ")[1]/td[2]//a[normalize-space(.)='Add Visit' and contains(@href, '/visits/new')]")

TestObject visitUnderFirstPet = new TestObject('visitUnderFirstPet')
visitUnderFirstPet.addProperty('xpath', ConditionType.EQUALS,
    '(' + firstPetRowXPath + ")[1]/td[2]//table//td[normalize-space(.)='" + visitDescription + "']")

TestObject visitUnderSecondPet = new TestObject('visitUnderSecondPet')
visitUnderSecondPet.addProperty('xpath', ConditionType.EQUALS,
    '(' + secondPetRowXPath + ")[1]/td[2]//table//td[normalize-space(.)='" + visitDescription + "']")

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

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'), firstPetName)

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

    WebUI.verifyElementVisible(firstPetRow)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add New Pet'), 10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add New Pet'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'), 10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'), secondPetName)

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

    WebUI.selectOptionByLabel(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), 'cat', false)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Pet'))

    WebUI.waitForPageLoad(10)

    WebUI.verifyElementVisible(firstPetRow)

    WebUI.verifyElementVisible(secondPetRow)

    WebUI.waitForElementVisible(firstPetAddVisitLink, 10)

    WebUI.click(firstPetAddVisitLink)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/Page_PetClinic  a Spring Framework demonstration/input_Description'),
        10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/Page_PetClinic  a Spring Framework demonstration/input_Description'),
        visitDescription)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Visit'))

    WebUI.waitForPageLoad(10)

    WebUI.verifyElementVisible(visitUnderFirstPet)

    WebUI.verifyElementNotPresent(visitUnderSecondPet, 2)
} finally {
    WebUI.closeBrowser()
}
