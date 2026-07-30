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
    WebUI.navigateToUrl(baseUrl + '/vets.html?page=1')

    TestObject specialtySpans = xpath('lindaSpecialtySpans',
        "//table[@id='vets']/tbody/tr[td[1][normalize-space(.)='Linda Douglas']]/td[2]/span")
    List elements = WebUI.findWebElements(specialtySpans, 10)
    List<String> specialtyNames = elements.collect { element -> element.getText().trim() }

    WebUI.verifyEqual(elements.size(), 2)
    WebUI.verifyEqual(specialtyNames, ['dentistry', 'surgery'])
} finally {
    WebUI.closeBrowser()
}
