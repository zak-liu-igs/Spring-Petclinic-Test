import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String vetsUrl = 'http://localhost:8080/vets.html'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(vetsUrl)
    WebUI.waitForPageLoad(10)

    TestObject vetsTable = xpath('vetsTable', "//table[@id='vets']")
    WebUI.verifyElementPresent(vetsTable, 10)
    WebUI.verifyElementAttributeValue(vetsTable, 'class', 'table table-striped', 10)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('tableHead', "//table[@id='vets']/thead"), 10).size(), 1)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('tableBody', "//table[@id='vets']/tbody"), 10).size(), 1)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('headerCells', "//table[@id='vets']/thead/tr/th"), 10).size(), 2)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('bodyRows', "//table[@id='vets']/tbody/tr"), 10).size(), 5)
} finally {
    WebUI.closeBrowser()
}
