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
    WebUI.verifyEqual(specialties.size(), 6)

    WebUI.navigateToUrl(baseUrl + '/vets.html?page=2')
    WebUI.waitForPageLoad(10)
    specialties.addAll(WebUI.findWebElements(xpath('secondPageSpecialties',
        "//table[@id='vets']/tbody/tr/td[2]/span"), 10).collect { it.getText().trim() })

    WebUI.verifyEqual(specialties.size(), 7)
    WebUI.verifyEqual(specialties.toSet().sort(), ['dentistry', 'none', 'radiology', 'surgery'])
    WebUI.verifyEqual(specialties.every { it.length() > 0 }, true)
} finally {
    WebUI.closeBrowser()
}
