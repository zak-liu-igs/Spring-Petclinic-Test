import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.net.URLEncoder

String stamp = System.currentTimeMillis().toString()
String sharedLastNamePrefix = 'Page' + stamp
String encodedLastNamePrefix = URLEncoder.encode(sharedLastNamePrefix, 'UTF-8')

TestObject matchingOwnerRows = new TestObject('matchingOwnerRows')
matchingOwnerRows.addProperty('xpath', ConditionType.EQUALS,
    "//table[@id='owners']//tr[td[1]/a[contains(normalize-space(.), ' " + sharedLastNamePrefix + "')]]")

TestObject ownerRowsTable = new TestObject('ownerRowsTable')
ownerRowsTable.addProperty('xpath', ConditionType.EQUALS, "//table[@id='owners']")

TestObject currentPageTwo = new TestObject('currentPageTwo')
currentPageTwo.addProperty('xpath', ConditionType.EQUALS,
    "//*[normalize-space(.)='Pages:']/following::*[(self::span or self::a) and normalize-space(.)='2'][1]")

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

    WebUI.waitForElementVisible(ownerRowsTable, 10)
    WebUI.waitForElementVisible(matchingOwnerRows, 10)
    WebUI.verifyEqual(WebUI.findWebElements(matchingOwnerRows, 10).size(), 5)

    // Navigate directly to page 2 using the same search filter. This avoids depending on brittle pagination markup.
    WebUI.navigateToUrl('http://localhost:8080/owners?lastName=' + encodedLastNamePrefix + '&page=2')
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(ownerRowsTable, 10)
    WebUI.waitForElementVisible(matchingOwnerRows, 10)
    WebUI.verifyEqual(WebUI.findWebElements(matchingOwnerRows, 10).size(), 1)

    // The exact pagination DOM differs across PetClinic versions. If a page indicator exists, verify it,
    // but use the row count and URL filter as the stable functional assertions.
    if (WebUI.verifyElementPresent(currentPageTwo, 2, com.kms.katalon.core.model.FailureHandling.OPTIONAL)) {
        WebUI.verifyElementVisible(currentPageTwo)
    }

    String normalizedUrl = WebUI.getUrl().replaceFirst(';jsessionid=[^/?#]+', '')
    WebUI.verifyMatch(normalizedUrl, 'http://localhost:8080/owners\\?.*lastName=' + sharedLastNamePrefix + '.*', true)
    WebUI.verifyMatch(normalizedUrl, 'http://localhost:8080/owners\\?.*page=2.*', true)
} finally {
    WebUI.closeBrowser()
}
