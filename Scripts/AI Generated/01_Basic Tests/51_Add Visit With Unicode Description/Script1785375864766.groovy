import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String stamp = System.currentTimeMillis().toString()
String firstName = 'UnicodeFirst' + stamp
String lastName = 'UnicodeLast' + stamp
String address = stamp + ' Unicode Visit Street'
String city = 'UnicodeCity'
String telephone = stamp.substring(stamp.length() - 10)
String petName = 'UnicodePet' + stamp

// Include BMP Unicode characters and non-BMP characters via Java Unicode escapes.
// ChromeDriver cannot type non-BMP characters with sendKeys/WebUI.setText, so the description is injected with JavaScript below.
String visitDescription = 'Unicode visit café 漢字 العربية русский ' + '\uD83D\uDE00' + ' ' + '\uD83D\uDC36' + ' ' + stamp

TestObject addVisitLink = new TestObject('addVisitLink')
addVisitLink.addProperty('xpath', ConditionType.EQUALS,
    "//a[normalize-space(.)='Add Visit' and contains(@href, '/visits/new')]")

TestObject savedVisit = new TestObject('savedVisit')
savedVisit.addProperty('xpath', ConditionType.EQUALS,
    "//table[.//th[normalize-space(.)='Visit Date'] and .//th[normalize-space(.)='Description']]//td[normalize-space(.)=\"" +
    visitDescription + "\"]")

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
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", ['2024-01-01'])

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), 10)
    WebUI.selectOptionByLabel(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), 'dog', false)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyTextPresent(petName, false)

    WebUI.waitForElementVisible(addVisitLink, 10)
    WebUI.click(addVisitLink)

    TestObject descriptionInput = findTestObject('Page_PetClinic  a Spring Framework demonstration/Page_PetClinic  a Spring Framework demonstration/input_Description')
    WebUI.waitForElementVisible(descriptionInput, 10)

    // Use JavaScript instead of WebUI.setText/sendKeys because ChromeDriver cannot type non-BMP characters such as emoji.
    WebUI.executeJavaScript("""
var field = document.querySelector("input[name='description'], textarea[name='description'], #description");
if (!field) {
    throw new Error('Visit description field was not found');
}
field.value = arguments[0];
field.dispatchEvent(new Event('input', {bubbles:true}));
field.dispatchEvent(new Event('change', {bubbles:true}));
""", [visitDescription])
    WebUI.verifyElementAttributeValue(descriptionInput, 'value', visitDescription, 5)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Visit'))
    WebUI.waitForPageLoad(10)

    WebUI.verifyElementVisible(savedVisit)
    WebUI.verifyElementText(savedVisit, visitDescription)
} finally {
    WebUI.closeBrowser()
}
