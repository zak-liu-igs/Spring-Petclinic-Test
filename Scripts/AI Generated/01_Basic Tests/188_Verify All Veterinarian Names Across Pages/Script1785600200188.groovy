import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
List<String> expectedNames = [
    'James Carter',
    'Helen Leary',
    'Linda Douglas',
    'Rafael Ortega',
    'Henry Stevens',
    'Sharon Jenkins'
]

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def readVisibleNames = {
    List elements = WebUI.findWebElements(xpath('visibleVetNames',
        "//table[@id='vets']/tbody/tr/td[1]"), 10)
    elements.collect { element -> element.getText().trim() }
}

try {
    WebUI.openBrowser('')
    List<String> actualNames = []

    WebUI.navigateToUrl(baseUrl + '/vets.html?page=1')
    actualNames.addAll(readVisibleNames())

    WebUI.navigateToUrl(baseUrl + '/vets.html?page=2')
    actualNames.addAll(readVisibleNames())

    WebUI.verifyEqual(actualNames, expectedNames)
    WebUI.verifyEqual(actualNames.toSet().size(), 6)
} finally {
    WebUI.closeBrowser()
}
