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
String sharedLastNamePrefix = 'Group' + stamp
String firstNameA = 'GrpAF' + stamp
String lastNameA = sharedLastNamePrefix + 'A'
String addressA = stamp + ' Group A Street'
String cityA = 'GroupCityA'
String telephoneA = '1' + stamp.substring(stamp.length() - 9)
String firstNameB = 'GrpBF' + stamp
String lastNameB = sharedLastNamePrefix + 'B'
String addressB = stamp + ' Group B Street'
String cityB = 'GroupCityB'
String telephoneB = '2' + stamp.substring(stamp.length() - 9)

TestObject firstOwnerRow = new TestObject('firstOwnerRow')
firstOwnerRow.addProperty('xpath', ConditionType.EQUALS,
    "//table[@id='owners']//tr[" +
    "td[1]/a[normalize-space(.)='" + firstNameA + ' ' + lastNameA + "'] and " +
    "td[2][normalize-space(.)='" + addressA + "'] and " +
    "td[3][normalize-space(.)='" + cityA + "'] and " +
    "td[4][normalize-space(.)='" + telephoneA + "'] and td[5][not(normalize-space(.))]]")

TestObject secondOwnerRow = new TestObject('secondOwnerRow')
secondOwnerRow.addProperty('xpath', ConditionType.EQUALS,
    "//table[@id='owners']//tr[" +
    "td[1]/a[normalize-space(.)='" + firstNameB + ' ' + lastNameB + "'] and " +
    "td[2][normalize-space(.)='" + addressB + "'] and " +
    "td[3][normalize-space(.)='" + cityB + "'] and " +
    "td[4][normalize-space(.)='" + telephoneB + "'] and td[5][not(normalize-space(.))]]")

TestObject matchingOwnerRows = new TestObject('matchingOwnerRows')
matchingOwnerRows.addProperty('xpath', ConditionType.EQUALS,
    "//table[@id='owners']//tr[td[1]/a[contains(normalize-space(.), ' " + sharedLastNamePrefix + "')]]")

def createOwner = { String firstName, String lastName, String address, String city, String telephone ->
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
}

try {
    WebUI.openBrowser('')

    WebUI.navigateToUrl('http://localhost:8080')

    WebUI.waitForPageLoad(10)

    createOwner(firstNameA, lastNameA, addressA, cityA, telephoneA)

    createOwner(firstNameB, lastNameB, addressB, cityB, telephoneB)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), 10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), sharedLastNamePrefix)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Find Owner'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(firstOwnerRow, 10)

    WebUI.verifyElementVisible(firstOwnerRow)

    WebUI.verifyElementVisible(secondOwnerRow)

    WebUI.verifyEqual(WebUI.findWebElements(matchingOwnerRows, 10).size(), 2)
} finally {
    WebUI.closeBrowser()
}
