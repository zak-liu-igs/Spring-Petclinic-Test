import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String stamp = System.currentTimeMillis().toString()
String firstName = 'FuturePetFirst' + stamp
String lastName = 'FuturePetLast' + stamp
String address = stamp + ' Future Pet Street'
String city = 'FuturePetCity'
String telephone = stamp.substring(stamp.length() - 10)
String petName = 'FuturePet' + stamp
String futureBirthDate = '2030-01-01'
String petType = 'dog'

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl('http://localhost:8080')
    WebUI.waitForPageLoad(10)

    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'))
    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'), 10)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add Owner'))
    WebUI.waitForPageLoad(10)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), 10)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_First Name'), firstName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), lastName)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Address'), address)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_City'), city)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Telephone'), telephone)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Owner'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyTextPresent(firstName + ' ' + lastName, false)

    // Use the actual Add New Pet link instead of concatenating to the owner URL.
    // The owner URL can contain a ;jsessionid path parameter, and appending '/pets/new' after it may produce an invalid route.
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/a_Add New Pet'))
    WebUI.waitForPageLoad(10)
    WebUI.verifyTextPresent('New', false)
    WebUI.verifyTextPresent('Pet', false)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'), 10)
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_PetName'), petName)

    // Set input[type=date] through JavaScript to avoid browser/locale-dependent date typing.
    WebUI.executeJavaScript("""
var d = document.getElementById('birthDate');
if (!d) {
    throw new Error('Birth Date field was not found');
}
d.value = arguments[0];
d.dispatchEvent(new Event('input', {bubbles:true}));
d.dispatchEvent(new Event('change', {bubbles:true}));
""", [futureBirthDate])

    WebUI.verifyElementAttributeValue(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_Birth Date'), 'value', futureBirthDate, 5)

    WebUI.waitForElementVisible(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), 10)
    WebUI.selectOptionByLabel(findTestObject('Page_PetClinic  a Spring Framework demonstration/select_Type'), petType, false)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Add Pet'))
    WebUI.waitForPageLoad(10)

    // Negative validation expectation: the app must reject a future birth date and remain on the New Pet form.
    // Validation wording differs by PetClinic/Spring Validation version, so the stable assertion is that the route remains /pets/new.
    String currentUrl = WebUI.getUrl()
    if (!currentUrl.contains('/pets/new')) {
        KeywordUtil.markFailed("Future birth date was accepted. Expected to remain on the New Pet form, but navigated to: " + currentUrl)
    }

    WebUI.verifyTextPresent('New', false)
    WebUI.verifyTextPresent('Pet', false)
} finally {
    WebUI.closeBrowser()
}
