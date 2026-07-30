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
    WebUI.verifyElementPresent(xpath('visitForm',
        "//form[.//*[@id='date'] and .//*[@id='description'] and .//input[@name='petId']]"), 10)
    WebUI.verifyElementPresent(xpath('ownerColumn',
        "//table[contains(@class,'table-striped')]//th[normalize-space(.)='Owner']"), 10)

    TestObject ownerIdControls = xpath('ownerIdControls',
        "//form//*[@name='ownerId' or @id='ownerId' or @name='owner.id']")
    WebUI.verifyEqual(WebUI.findWebElements(ownerIdControls, 2).size(), 0)
} finally {
    WebUI.closeBrowser()
}
