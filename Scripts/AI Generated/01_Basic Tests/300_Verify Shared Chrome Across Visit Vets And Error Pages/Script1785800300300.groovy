import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'
List<String> routes = ['/owners/6/pets/7/visits/new', '/vets.html', '/oups']

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')

    for (String route : routes) {
        WebUI.navigateToUrl(baseUrl + route)
        WebUI.waitForPageLoad(10)

        WebUI.verifyEqual(WebUI.getWindowTitle(), 'PetClinic :: a Spring Framework demonstration')
        WebUI.verifyEqual(WebUI.findWebElements(xpath('primaryNavigation-' + route,
            "//nav//ul[contains(concat(' ', normalize-space(@class), ' '), ' navbar-nav ')]/li/a"), 10).size(), 4)
        WebUI.verifyElementPresent(xpath('footerLogo-' + route,
            "//div[contains(concat(' ', normalize-space(@class), ' '), ' text-center ')]" +
            "//img[@src='/resources/images/spring-logo.svg' and @alt='VMware Tanzu Logo']"), 10)
        WebUI.verifyElementPresent(xpath('viewport-' + route,
            "//meta[@name='viewport' and contains(@content, 'width=device-width')]"), 10)
        WebUI.verifyElementPresent(xpath('stylesheet-' + route,
            "//link[@rel='stylesheet' and @href='/resources/css/petclinic.css']"), 10)
    }
} finally {
    WebUI.closeBrowser()
}
