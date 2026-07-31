import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String samanthaVisitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String maxVisitUrl = 'http://localhost:8080/owners/6/pets/8/visits/new'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(samanthaVisitUrl)
    WebUI.waitForPageLoad(10)

    TestObject petSummaryRow = xpath('petSummaryRow',
        "//b[normalize-space(.)='Pet']/following-sibling::table[1]//tr[td]")
    WebUI.verifyElementText(xpath('samanthaName',
        "//b[normalize-space(.)='Pet']/following-sibling::table[1]//tr[td]/td[1]"), 'Samantha')
    WebUI.verifyElementText(xpath('samanthaOwner',
        "//b[normalize-space(.)='Pet']/following-sibling::table[1]//tr[td]/td[4]"), 'Jean Coleman')
    WebUI.verifyElementAttributeValue(xpath('samanthaPetId', "//form//input[@type='hidden' and @name='petId']"),
        'value', '7', 10)
    WebUI.verifyEqual(WebUI.findWebElements(petSummaryRow, 10).size(), 1)

    WebUI.navigateToUrl(maxVisitUrl)
    WebUI.waitForPageLoad(10)

    WebUI.verifyElementText(xpath('maxName',
        "//b[normalize-space(.)='Pet']/following-sibling::table[1]//tr[td]/td[1]"), 'Max')
    WebUI.verifyElementText(xpath('maxOwner',
        "//b[normalize-space(.)='Pet']/following-sibling::table[1]//tr[td]/td[4]"), 'Jean Coleman')
    WebUI.verifyElementAttributeValue(xpath('maxPetId', "//form//input[@type='hidden' and @name='petId']"),
        'value', '8', 10)
    WebUI.verifyEqual(WebUI.findWebElements(petSummaryRow, 10).size(), 1)
} finally {
    WebUI.closeBrowser()
}
