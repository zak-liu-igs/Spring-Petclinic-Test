import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebElement

String ownerUrl = 'http://localhost:8080/owners/6'

TestObject visitDates = new TestObject('visitDates')
visitDates.addProperty('xpath', ConditionType.EQUALS,
    "//h2[normalize-space(.)='Pets and Visits']/following-sibling::table[1]//tr[" +
    "td[1]//dl and td[2]//a[contains(@href, '/owners/6/pets/7/visits/new')]]" +
    "/td[2]//table[contains(concat(' ', normalize-space(@class), ' '), ' table-condensed ')]" +
    "//tr[td and not(.//a)]/td[1]")

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(ownerUrl)
    WebUI.waitForPageLoad(10)

    List<WebElement> dateElements = WebUI.findWebElements(visitDates, 10)
    WebUI.verifyEqual(dateElements.isEmpty(), false)

    for (WebElement dateElement : dateElements) {
        WebUI.verifyMatch(dateElement.getText().trim(), '\\d{4}-\\d{2}-\\d{2}', true)
    }
} finally {
    WebUI.closeBrowser()
}
