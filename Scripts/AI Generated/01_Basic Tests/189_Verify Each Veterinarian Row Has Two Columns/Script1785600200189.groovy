import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

def verifyPageRows = { int page, int expectedRows ->
    WebUI.navigateToUrl(baseUrl + '/vets.html?page=' + page)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('rowsOnPage' + page,
        "//table[@id='vets']/tbody/tr"), 10).size(), expectedRows)

    for (int row = 1; row <= expectedRows; row++) {
        WebUI.verifyEqual(WebUI.findWebElements(xpath('page' + page + 'Row' + row + 'Cells',
            "//table[@id='vets']/tbody/tr[" + row + "]/td"), 10).size(), 2)
    }
}

try {
    WebUI.openBrowser('')
    verifyPageRows(1, 5)
    verifyPageRows(2, 1)
} finally {
    WebUI.closeBrowser()
}
