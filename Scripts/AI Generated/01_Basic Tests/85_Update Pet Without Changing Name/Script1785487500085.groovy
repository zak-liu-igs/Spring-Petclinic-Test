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
String firstName = 'SameNameF' + stamp
String lastName = 'SameNameL' + stamp
String address = stamp + ' Same Name Update Street'
String city = 'SameNameCity'
String telephone = '7' + stamp.substring(stamp.length() - 9)
String petName = 'Unchanged' + stamp
String originalType = 'dog'
String updatedType = 'bird'

String originalPetRowXPath = "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]//tr[" +
    "td[1]//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "']]"

TestObject scopedEditPetLink = new TestObject('scopedEditPetLink')
scopedEditPetLink.addProperty('xpath', ConditionType.EQUALS,
    '(' + originalPetRowXPath + ")[1]/td[2]//a[normalize-space(.)='Edit Pet']")

TestObject updateSuccessMessage = new TestObject('updateSuccessMessage')
updateSuccessMessage.addProperty('xpath', ConditionType.EQUALS,
    "//div[@id='success-message']/span[normalize-space(.)='Pet details has been edited']")

TestObject updatedPetRow = new TestObject('updatedPetRow')
updatedPetRow.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]//tr[" +
    "td[1]//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='" + petName + "'] and " +
    "td[1]//dt[normalize-space(.)='Birth Date']/following-sibling::dd[1][normalize-space(.)='2024-01-01'] and " +
    "td[1]//dt[normalize-space(.)='Type']/following-sibling::dd[1][normalize-space(.)='" + updatedType + "']]")

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

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add New Pet'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'), 10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'), petName)

    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = '2024-01-01';
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", null)

    WebUI.selectOptionByLabel(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), originalType, false)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Pet'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(scopedEditPetLink, 10)

    WebUI.click(scopedEditPetLink)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Update Pet'), 10)

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'),
        'value', petName, 10)

    WebUI.selectOptionByLabel(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), updatedType, false)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Update Pet'))

    WebUI.waitForElementVisible(updateSuccessMessage, 3)

    WebUI.verifyElementText(updateSuccessMessage, 'Pet details has been edited')

    WebUI.waitForElementVisible(updatedPetRow, 10)

    WebUI.verifyElementVisible(updatedPetRow)
} finally {
    WebUI.closeBrowser()
}
