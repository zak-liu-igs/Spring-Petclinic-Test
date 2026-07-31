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

    List<String> visitFormHistory = WebUI.findWebElements(xpath('samanthaVisitFormRows',
        "//b[normalize-space(.)='Previous Visits']/following-sibling::table[1]//tr[td]"), 10).collect {
        it.getText().trim().replaceAll(/\s+/, ' ')
    }
    WebUI.verifyEqual(visitFormHistory.size() >= 2, true)

    WebUI.navigateToUrl(baseUrl + '/owners/6')
    WebUI.waitForPageLoad(10)
    List<String> ownerDetailsHistory = WebUI.findWebElements(xpath('samanthaOwnerRows',
        "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]" +
        "//tr[td[1]//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='Samantha']]" +
        "//table[contains(concat(' ', normalize-space(@class), ' '), ' table-condensed ')]" +
        "//tr[td and not(.//a)]"), 10).collect {
        it.getText().trim().replaceAll(/\s+/, ' ')
    }

    WebUI.verifyEqual(ownerDetailsHistory.size(), visitFormHistory.size())
    WebUI.verifyEqual(ownerDetailsHistory, visitFormHistory)
    WebUI.verifyEqual(ownerDetailsHistory.contains('2013-01-04 spayed'), true)
} finally {
    WebUI.closeBrowser()
}
