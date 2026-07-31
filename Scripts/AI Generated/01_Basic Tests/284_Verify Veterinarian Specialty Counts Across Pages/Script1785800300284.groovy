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
    WebUI.navigateToUrl(baseUrl + '/vets.html')
    WebUI.waitForPageLoad(10)

    List<String> specialties = WebUI.findWebElements(xpath('firstPageSpecialties',
        "//table[@id='vets']/tbody/tr/td[2]/span"), 10).collect { it.getText().trim() }
    int veterinarianCount = WebUI.findWebElements(xpath('firstPageRows', "//table[@id='vets']/tbody/tr"), 10).size()

    WebUI.navigateToUrl(baseUrl + '/vets.html?page=2')
    WebUI.waitForPageLoad(10)
    specialties.addAll(WebUI.findWebElements(xpath('secondPageSpecialties',
        "//table[@id='vets']/tbody/tr/td[2]/span"), 10).collect { it.getText().trim() })
    veterinarianCount += WebUI.findWebElements(xpath('secondPageRows', "//table[@id='vets']/tbody/tr"), 10).size()

    WebUI.verifyEqual(veterinarianCount, 6)
    WebUI.verifyEqual(specialties.count('none'), 2)
    WebUI.verifyEqual(specialties.count('radiology'), 2)
    WebUI.verifyEqual(specialties.count('surgery'), 2)
    WebUI.verifyEqual(specialties.count('dentistry'), 1)
    WebUI.verifyEqual(specialties.size(), 7)
} finally {
    WebUI.closeBrowser()
}
