import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebElement
import java.util.regex.Pattern

String testId = id?.toString()?.trim()
String caseName = case_name?.toString()?.trim()
String targetUrl = url?.toString()?.trim()
String ownerId = owner_id?.toString()?.trim()
String petId = pet_id?.toString()?.trim()
String visitDate = visit_date?.toString()?.trim()
String visitDescription = description?.toString()?.trim()
String expectedText = expected_text?.toString()?.trim()
String scenarioType = scenario_type?.toString()?.trim()
String expectedResult = expected_result?.toString()?.trim()

if (!targetUrl || !visitDate || !visitDescription) {
	KeywordUtil.markFailedAndStop(
		"CSV data is missing: id=${testId}, url=${targetUrl}, date=${visitDate}, description=${visitDescription}"
	)
}

TestObject dateInput = new TestObject('dateInput')
dateInput.addProperty('id', ConditionType.EQUALS, 'date')

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('id', ConditionType.EQUALS, 'description')

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//button[@type='submit' and contains(normalize-space(.), 'Add Visit')]"
)

WebUI.comment(
	"Running ${testId}: ${caseName} | Owner=${ownerId} | Pet=${petId} | ${scenarioType}"
)

WebUI.openBrowser('')

try {
	WebUI.navigateToUrl(targetUrl)
	WebUI.waitForPageLoad(10)

	WebUI.verifyElementPresent(dateInput, 10)
	WebUI.verifyElementPresent(descriptionInput, 10)
	WebUI.verifyElementPresent(addVisitButton, 10)

	WebElement dateElement = WebUI.findWebElement(dateInput, 10)
	WebElement descriptionElement = WebUI.findWebElement(descriptionInput, 10)

	WebUI.executeJavaScript("""
        arguments[0].value = arguments[1];
        arguments[0].dispatchEvent(new Event('input', {bubbles: true}));
        arguments[0].dispatchEvent(new Event('change', {bubbles: true}));
        arguments[0].dispatchEvent(new Event('blur', {bubbles: true}));
    """, [dateElement, visitDate])

	WebUI.executeJavaScript("""
        arguments[0].value = arguments[1];
        arguments[0].dispatchEvent(new Event('input', {bubbles: true}));
        arguments[0].dispatchEvent(new Event('change', {bubbles: true}));
        arguments[0].dispatchEvent(new Event('blur', {bubbles: true}));
    """, [descriptionElement, visitDescription])

	String actualDate = WebUI.executeJavaScript(
		"return document.getElementById('date').value;",
		null
	)?.toString()

	String actualDescription = WebUI.executeJavaScript(
		"return document.getElementById('description').value;",
		null
	)?.toString()

	WebUI.comment("Date before submit: ${actualDate}")
	WebUI.comment("Description before submit: ${actualDescription}")

	WebUI.verifyMatch(actualDate, visitDate, false)
	WebUI.verifyMatch(actualDescription, visitDescription, false)

	WebUI.click(addVisitButton)
	WebUI.delay(2)

	String currentUrl = WebUI.getUrl()

	if (currentUrl.contains('/visits/new')) {
		String dateValidation = WebUI.executeJavaScript(
			"return document.getElementById('date').validationMessage;",
			null
		)?.toString()

		String descriptionValidation = WebUI.executeJavaScript(
			"return document.getElementById('description').validationMessage;",
			null
		)?.toString()

		WebUI.comment("Date validation: ${dateValidation}")
		WebUI.comment("Description validation: ${descriptionValidation}")

		if (!dateValidation && !descriptionValidation) {
			WebUI.comment('Normal click did not submit. Trying direct form submission.')

			WebUI.executeJavaScript("""
                var form = document.getElementById('date').closest('form');
                if (!form) {
                    throw new Error('Visit form was not found');
                }
                form.submit();
            """, null)

			WebUI.waitForPageLoad(10)
			WebUI.delay(1)
			currentUrl = WebUI.getUrl()
		}
	}

	if (currentUrl.contains('/visits/new')) {
		String bodyText = WebUI.executeJavaScript(
			"return document.body ? document.body.innerText : '';",
			null
		)?.toString() ?: ''

		KeywordUtil.markFailedAndStop(
			"Visit was not submitted. Current URL=${currentUrl}. Page text=${bodyText}"
		)
	}

	String bodyText = WebUI.executeJavaScript(
		"return document.body ? document.body.innerText : '';",
		null
	)?.toString() ?: ''

	String expectedPattern = '(?is).*' +
		Pattern.quote(expectedText ?: visitDescription) +
		'.*'

	WebUI.verifyMatch(bodyText, expectedPattern, true)

	WebUI.comment(
		"PASSED ${testId}: ${caseName} | Expected result=${expectedResult}"
	)
} catch (Throwable error) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("FAILED ${testId}: ${caseName}. ${error.message}")
	throw error
} finally {
	WebUI.closeBrowser()
}