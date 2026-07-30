import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
List<String> expectedNames = [
    'James Carter',
    'Helen Leary',
    'Linda Douglas',
    'Rafael Ortega',
    'Henry Stevens'
]

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/vets.html?page=1')
    List<String> actualNames = []

    for (int row = 1; row <= expectedNames.size(); row++) {
        actualNames.add(WebUI.getText(xpath('vetName' + row,
            "//table[@id='vets']/tbody/tr[" + row + "]/td[1]")).trim())
    }

    WebUI.verifyEqual(actualNames, expectedNames)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('veterinarianRows',
        "//table[@id='vets']/tbody/tr"), 10).size(), 5)
} finally {
    WebUI.closeBrowser()
}
