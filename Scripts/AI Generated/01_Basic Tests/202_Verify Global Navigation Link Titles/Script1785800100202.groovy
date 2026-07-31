import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

Map<String, String> expectedTitles = [
    'Home': 'home page',
    'Find Owners': 'find owners',
    'Veterinarians': 'veterinarians',
    'Error': 'trigger a RuntimeException to see how it is handled'
]

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl)

    WebUI.verifyEqual(WebUI.findWebElements(xpath('titledNavigationLinks',
        "//*[@id='main-navbar']//a[contains(@class,'nav-link') and @title]"), 10).size(), 4)
    expectedTitles.each { String label, String expectedTitle ->
        TestObject navigationLink = xpath('navigationLink' + label.replace(' ', ''),
            "//nav//a[contains(@class,'nav-link') and .//span[normalize-space(.)='" + label + "']]")
        WebUI.verifyElementAttributeValue(navigationLink, 'title', expectedTitle, 10)
    }
} finally {
    WebUI.closeBrowser()
}
