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

    TestObject headers = xpath('veterinarianHeaders', "//table[@id='vets']/thead/tr/th")
    WebUI.verifyEqual(WebUI.findWebElements(headers, 10).size(), 2)
    WebUI.verifyElementText(xpath('nameHeader', "//table[@id='vets']/thead/tr/th[1]"), 'Name')
    WebUI.verifyElementText(xpath('specialtiesHeader', "//table[@id='vets']/thead/tr/th[2]"), 'Specialties')
} finally {
    WebUI.closeBrowser()
}
