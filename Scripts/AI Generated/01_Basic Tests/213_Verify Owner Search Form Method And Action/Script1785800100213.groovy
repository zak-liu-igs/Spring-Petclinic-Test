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
    WebUI.navigateToUrl(baseUrl + '/owners/find')

    TestObject searchForm = xpath('ownerSearchForm',
        "//form[@id='search-owner-form' and .//*[@id='lastName']]")
    WebUI.verifyElementPresent(searchForm, 10)
    WebUI.verifyElementAttributeValue(searchForm, 'method', 'get', 10)
    WebUI.verifyElementAttributeValue(searchForm, 'action', baseUrl + '/owners', 10)
    WebUI.verifyElementPresent(xpath('searchSubmit',
        "//form[@id='search-owner-form']//button[@type='submit' and normalize-space(.)='Find Owner']"), 10)
} finally {
    WebUI.closeBrowser()
}
