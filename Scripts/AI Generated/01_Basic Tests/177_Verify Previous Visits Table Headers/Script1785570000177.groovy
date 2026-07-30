import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebElement

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

TestObject previousVisitHeaders = new TestObject('previousVisitHeaders')
previousVisitHeaders.addProperty('xpath', ConditionType.EQUALS,
    "//b[normalize-space(.)='Previous Visits']/following-sibling::table[1]//tr[1]/th")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)

    List<WebElement> headers = WebUI.findWebElements(previousVisitHeaders, 10)
    WebUI.verifyEqual(headers.size(), 2)
    WebUI.verifyEqual(headers[0].getText().trim(), 'Date')
    WebUI.verifyEqual(headers[1].getText().trim(), 'Description')
} finally {
    WebUI.closeBrowser()
}
