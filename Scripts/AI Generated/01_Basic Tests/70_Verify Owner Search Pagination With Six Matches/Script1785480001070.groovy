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
String sharedLastNamePrefix = 'Page' + stamp

TestObject matchingOwnerRows = new TestObject('matchingOwnerRows')
matchingOwnerRows.addProperty('xpath', ConditionType.EQUALS,
    "//table[@id='owners']//tr[td[1]/a[contains(normalize-space(.), ' " + sharedLastNamePrefix + "')]]")

TestObject currentPageTwo = new TestObject('currentPageTwo')
currentPageTwo.addProperty('xpath', ConditionType.EQUALS,
    "//div[.//span[normalize-space(.)='Pages:']]/span/span[normalize-space(.)='2' and not(*)]")

def createOwner = { int index ->
    String firstName = 'PgF' + index + stamp
    String lastName = sharedLastNamePrefix + index
    String address = stamp + ' Page ' + index + ' Street'
    String city = 'PageCity' + index
    String telephone = index.toString() + stamp.substring(stamp.length() - 9)

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

    (1..6).each { int index ->
        createOwner(index)
    }

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'))

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), 10)

    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), sharedLastNamePrefix)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Find Owner'))

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(matchingOwnerRows, 10)

    WebUI.verifyEqual(WebUI.findWebElements(matchingOwnerRows, 10).size(), 5)

    WebUI.navigateToUrl('http://localhost:8080/owners?lastName=' + sharedLastNamePrefix + '&page=2')

    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(matchingOwnerRows, 10)

    WebUI.verifyEqual(WebUI.findWebElements(matchingOwnerRows, 10).size(), 1)

    WebUI.verifyElementVisible(currentPageTwo)

    WebUI.verifyMatch(WebUI.getUrl(),
        'http://localhost:8080/owners\\?lastName=' + sharedLastNamePrefix + '&page=2', true)
} finally {
    WebUI.closeBrowser()
}
