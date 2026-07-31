import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

Map<String, String> expectedTargets = [
    'Home': baseUrl + '/',
    'Find Owners': baseUrl + '/owners/find',
    'Veterinarians': baseUrl + '/vets.html',
    'Error': baseUrl + '/oups'
]

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl)

    WebUI.verifyEqual(WebUI.findWebElements(xpath('linkedNavigationItems',
        "//*[@id='main-navbar']//a[contains(@class,'nav-link') and @href]"), 10).size(), 4)
    expectedTargets.each { String label, String expectedTarget ->
        TestObject navigationLink = xpath('navigationTarget' + label.replace(' ', ''),
            "//nav//a[contains(@class,'nav-link') and .//span[normalize-space(.)='" + label + "']]")
        WebUI.verifyElementAttributeValue(navigationLink, 'href', expectedTarget, 10)
    }
} finally {
    WebUI.closeBrowser()
}
