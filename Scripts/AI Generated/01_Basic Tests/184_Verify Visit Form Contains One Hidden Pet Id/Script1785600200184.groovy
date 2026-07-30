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
    WebUI.navigateToUrl(baseUrl + '/owners/6/pets/7/visits/new')
    WebUI.verifyElementPresent(xpath('newVisitHeading', "//h2[normalize-space(.)='New Visit']"), 10)

    TestObject hiddenPetId = xpath('hiddenPetId',
        "//form[.//*[@id='date'] and .//*[@id='description']]//input[@type='hidden' and @name='petId']")
    WebUI.verifyEqual(WebUI.findWebElements(hiddenPetId, 10).size(), 1)
    WebUI.verifyElementAttributeValue(hiddenPetId, 'value', '7', 10)
} finally {
    WebUI.closeBrowser()
}
