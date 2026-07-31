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
    WebUI.navigateToUrl(baseUrl)

    TestObject petImage = xpath('homePetImage',
        "//h2[normalize-space(.)='Welcome']/following::img[contains(@src,'/resources/images/pets.png')][1]")
    WebUI.verifyElementVisible(petImage)
    WebUI.verifyMatch(WebUI.getAttribute(petImage, 'class'), '(^|.*\\s)img-responsive(\\s.*|$)', true)
    WebUI.verifyMatch(WebUI.getAttribute(petImage, 'src'),
        '^http://localhost:8080/resources/images/pets\\.png$', true)
    WebUI.verifyEqual(WebUI.executeJavaScript("""
var image = document.querySelector("img[src*='/resources/images/pets.png']");
return Boolean(image && image.complete && image.naturalWidth > 0 && image.naturalHeight > 0);
""", null), true)
} finally {
    WebUI.closeBrowser()
}
