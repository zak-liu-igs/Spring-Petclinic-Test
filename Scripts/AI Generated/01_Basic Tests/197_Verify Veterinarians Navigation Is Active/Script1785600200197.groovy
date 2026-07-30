import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl)
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Veterinarians'))

    TestObject activeVeterinarians = xpath('activeVeterinarians',
        "//nav//a[contains(concat(' ', normalize-space(@class), ' '), ' active ')" +
        " and .//span[normalize-space(.)='Veterinarians']]")
    WebUI.verifyElementPresent(activeVeterinarians, 10)
    WebUI.verifyElementPresent(xpath('veterinariansHeading',
        "//h2[normalize-space(.)='Veterinarians']"), 10)
} finally {
    WebUI.closeBrowser()
}
