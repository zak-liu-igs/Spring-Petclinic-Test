import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebElement

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'

TestObject metadataHeaders = new TestObject('metadataHeaders')
metadataHeaders.addProperty('xpath', ConditionType.EQUALS,
    "//b[normalize-space(.)='Pet']/following-sibling::table[1]/thead/tr/th")

TestObject metadataValues = new TestObject('metadataValues')
metadataValues.addProperty('xpath', ConditionType.EQUALS,
    "//b[normalize-space(.)='Pet']/following-sibling::table[1]/tbody/tr[1]/td")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)

    List<WebElement> headers = WebUI.findWebElements(metadataHeaders, 10)
    List<WebElement> values = WebUI.findWebElements(metadataValues, 10)

    WebUI.verifyEqual(headers.size(), 4)
    WebUI.verifyEqual(headers[0].getText().trim(), 'Name')
    WebUI.verifyEqual(headers[1].getText().trim(), 'Birth Date')
    WebUI.verifyEqual(headers[2].getText().trim(), 'Type')
    WebUI.verifyEqual(headers[3].getText().trim(), 'Owner')

    WebUI.verifyEqual(values.size(), 4)
    WebUI.verifyEqual(values[0].getText().trim(), 'Samantha')
    WebUI.verifyMatch(values[1].getText().trim(), '\\d{4}-\\d{2}-\\d{2}', true)
    WebUI.verifyEqual(values[2].getText().trim(), 'cat')
    WebUI.verifyEqual(values[3].getText().trim(), 'Jean Coleman')
} finally {
    WebUI.closeBrowser()
}
