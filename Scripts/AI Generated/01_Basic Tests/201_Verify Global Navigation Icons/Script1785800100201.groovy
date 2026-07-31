import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

Map<String, String> navigationIcons = [
    'Home': 'fa-home',
    'Find Owners': 'fa-search',
    'Veterinarians': 'fa-th-list',
    'Error': 'fa-exclamation-triangle'
]

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl)

    WebUI.verifyEqual(WebUI.findWebElements(xpath('primaryNavigationLinks',
        "//*[@id='main-navbar']//a[contains(@class,'nav-link')]"), 10).size(), 4)
    navigationIcons.each { String label, String iconClass ->
        WebUI.verifyElementPresent(xpath('navigationIcon' + label.replace(' ', ''),
            "//nav//a[contains(@class,'nav-link') and .//span[normalize-space(.)='" + label + "']]" +
            "/span[contains(concat(' ', normalize-space(@class), ' '), ' " + iconClass + " ')]"), 10)
    }
} finally {
    WebUI.closeBrowser()
}
