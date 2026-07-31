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

    List<String> firstPageNames = WebUI.findWebElements(xpath('firstPageNames',
        "//table[@id='vets']/tbody/tr/td[1]"), 10).collect { it.getText().trim() }
    WebUI.verifyEqual(firstPageNames.size(), 5)
    WebUI.verifyEqual(firstPageNames.every { it.split(/\s+/).size() == 2 }, true)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('firstPageNameLinks',
        "//table[@id='vets']/tbody/tr/td[1]//a"), 10).size(), 0)

    WebUI.navigateToUrl(baseUrl + '/vets.html?page=2')
    WebUI.waitForPageLoad(10)
    List<String> secondPageNames = WebUI.findWebElements(xpath('secondPageNames',
        "//table[@id='vets']/tbody/tr/td[1]"), 10).collect { it.getText().trim() }

    WebUI.verifyEqual(secondPageNames, ['Sharon Jenkins'])
    WebUI.verifyEqual(secondPageNames.every { it.split(/\s+/).size() == 2 }, true)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('secondPageNameLinks',
        "//table[@id='vets']/tbody/tr/td[1]//a"), 10).size(), 0)
} finally {
    WebUI.closeBrowser()
}
