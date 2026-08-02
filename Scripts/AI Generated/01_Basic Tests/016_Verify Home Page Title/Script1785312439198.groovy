import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = url?.trim() ?: 'http://localhost:8080'
String title = expectedTitle?.trim() ?: 'PetClinic :: a Spring Framework demonstration'
String homeText = expectedHomeText?.trim() ?: 'Home'
String findOwnersText = expectedFindOwnersText?.trim() ?: 'Find owners'
String veterinariansText = expectedVeterinariansText?.trim() ?: 'Veterinarians'
String executedCaseName = caseName?.trim() ?: '016_Verify Home Page Title'

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl)
    WebUI.waitForPageLoad(30)

    WebUI.verifyMatch(WebUI.getWindowTitle(), title, false)
    WebUI.verifyElementText(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Home'), homeText)
    WebUI.verifyElementText(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Find Owners'), findOwnersText)
    WebUI.verifyElementText(findTestObject('Page_PetClinic  a Spring Framework demonstration/span_Veterinarians'), veterinariansText)

    WebUI.comment("Executed ${executedCaseName}")
} finally {
    WebUI.closeBrowser()
}
