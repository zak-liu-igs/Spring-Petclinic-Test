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
    WebUI.waitForPageLoad(10)

    WebUI.verifyElementPresent(xpath('samanthaSpayed',
        "//b[normalize-space(.)='Previous Visits']/following-sibling::table[1]//tr[td[2][normalize-space(.)='spayed']]"), 10)
    WebUI.verifyElementNotPresent(xpath('samanthaNoNeutered',
        "//b[normalize-space(.)='Previous Visits']/following-sibling::table[1]//tr[td[2][normalize-space(.)='neutered']]"), 2)

    WebUI.navigateToUrl(baseUrl + '/owners/6/pets/8/visits/new')
    WebUI.waitForPageLoad(10)

    WebUI.verifyElementPresent(xpath('maxNeutered',
        "//b[normalize-space(.)='Previous Visits']/following-sibling::table[1]//tr[td[2][normalize-space(.)='neutered']]"), 10)
    WebUI.verifyElementNotPresent(xpath('maxNoSpayed',
        "//b[normalize-space(.)='Previous Visits']/following-sibling::table[1]//tr[td[2][normalize-space(.)='spayed']]"), 2)
} finally {
    WebUI.closeBrowser()
}
