import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebElement

String ownerUrl = 'http://localhost:8080/owners/6'

TestObject visitHeaders = new TestObject('visitHeaders')
visitHeaders.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]//tr[" +
    "td[1]//dt[normalize-space(.)='Name']/following-sibling::dd[1][normalize-space(.)='Samantha']]" +
    "/td[2]//table[contains(concat(' ', normalize-space(@class), ' '), ' table-condensed ')]/thead/tr/th")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(ownerUrl)
    WebUI.waitForPageLoad(10)

    List<WebElement> headers = WebUI.findWebElements(visitHeaders, 10)
    WebUI.verifyEqual(headers.size(), 2)
    WebUI.verifyEqual(headers[0].getText().trim(), 'Visit Date')
    WebUI.verifyEqual(headers[1].getText().trim(), 'Description')
} finally {
    WebUI.closeBrowser()
}
