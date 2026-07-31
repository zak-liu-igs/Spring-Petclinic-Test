import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String errorUrl = 'http://localhost:8080/oups'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(errorUrl)
    WebUI.waitForPageLoad(10)

    TestObject petImage = xpath('petImage',
        "//h2[normalize-space(.)='Something happened...']/preceding-sibling::img[1]")
    TestObject footerLogo = xpath('footerLogo',
        "//div[contains(concat(' ', normalize-space(@class), ' '), ' text-center ')]" +
        "//img[contains(concat(' ', normalize-space(@class), ' '), ' logo ')]")

    WebUI.verifyElementVisible(petImage)
    WebUI.verifyMatch(WebUI.getAttribute(petImage, 'src'), '.*/resources/images/pets\\.png$', true)
    WebUI.verifyElementVisible(footerLogo)
    WebUI.verifyMatch(WebUI.getAttribute(footerLogo, 'src'), '.*/resources/images/spring-logo\\.svg$', true)
    WebUI.verifyElementAttributeValue(footerLogo, 'alt', 'VMware Tanzu Logo', 10)
    WebUI.verifyEqual(WebUI.findWebElements(xpath('errorContentImages',
        "//div[contains(concat(' ', normalize-space(@class), ' '), ' xd-container ')]//img"), 10).size(), 2)
} finally {
    WebUI.closeBrowser()
}
