import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Map<String, Object> data = [:]

binding.variables.each { key, value ->
	String normalizedKey = key.toString()
		.replaceAll(/[\r\n\t]/, '')
		.trim()

	data[normalizedKey] = value
}

List<String> requiredVariables = [
	'id',
	'case_name',
	'url',
	'owner_id',
	'pet_id',
	'visit_date',
	'description',
	'invalid_field',
	'expected_error_key',
	'scenario_type',
	'expected_result'
]

List<String> missingVariables = requiredVariables.findAll {
	!data.containsKey(it)
}

if (!missingVariables.isEmpty()) {
	KeywordUtil.markFailedAndStop(
		"Missing CSV variables: ${missingVariables}. " +
		"Available variables: ${binding.variables.keySet()}"
	)
}

String testId = data['id']?.toString()?.trim() ?: ''
String caseName = data['case_name']?.toString()?.trim() ?: ''
String targetUrl = data['url']?.toString()?.trim() ?: ''
String ownerId = data['owner_id']?.toString()?.trim() ?: ''
String petId = data['pet_id']?.toString()?.trim() ?: ''
String visitDateValue = data['visit_date']?.toString()?.trim() ?: ''
String descriptionValue = data['description']?.toString() ?: ''
String invalidField = data['invalid_field']?.toString()?.trim() ?: ''
String expectedErrorKey = data['expected_error_key']?.toString()?.trim() ?: ''
String scenarioType = data['scenario_type']?.toString()?.trim() ?: ''
String expectedResult = data['expected_result']?.toString()?.trim() ?: ''

if (!testId || !caseName || !targetUrl || !ownerId ||
	!petId || !visitDateValue || !invalidField ||
	!expectedErrorKey || !scenarioType || !expectedResult) {

	KeywordUtil.markFailedAndStop(
		"CSV row contains incomplete data. " +
		"id=${testId}, case=${caseName}, url=${targetUrl}, " +
		"owner_id=${ownerId}, pet_id=${petId}, " +
		"visit_date=${visitDateValue}, invalid_field=${invalidField}"
	)
}

Map<String, String> errorMessages = [
	'future_date' : 'Visit date must be in the future',
	'required'    : 'must not be blank'
]

String expectedErrorMessage =
	errorMessages[expectedErrorKey] ?: expectedErrorKey

TestObject dateInput = new TestObject('dateInput')
dateInput.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[@id='date' or @name='date']"
)

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[@id='description' or @name='description']"
)

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//button[@type='submit' and contains(normalize-space(.), 'Add Visit')] | " +
	"//input[@type='submit']"
)

KeywordUtil.logInfo(
	"Running ${caseName} (${testId}) | " +
	"date=${visitDateValue} | description='${descriptionValue}' | " +
	"scenario=${scenarioType} | expected=${expectedErrorMessage}"
)

WebUI.openBrowser('')

try {
	WebUI.navigateToUrl(targetUrl)
	WebUI.waitForPageLoad(30)

	WebUI.waitForElementVisible(dateInput, 10)
	WebUI.waitForElementVisible(descriptionInput, 10)
	WebUI.waitForElementVisible(addVisitButton, 10)

	WebUI.executeJavaScript(
		"""
		var dateField = document.getElementById('date');
		var descriptionField = document.getElementById('description');

		if (!dateField) {
			throw new Error('Visit date field was not found');
		}

		if (!descriptionField) {
			throw new Error('Description field was not found');
		}

		dateField.value = arguments[0];
		dateField.dispatchEvent(
			new Event('input', { bubbles: true })
		);
		dateField.dispatchEvent(
			new Event('change', { bubbles: true })
		);

		descriptionField.value = arguments[1];
		descriptionField.dispatchEvent(
			new Event('input', { bubbles: true })
		);
		descriptionField.dispatchEvent(
			new Event('change', { bubbles: true })
		);
		""",
		[visitDateValue, descriptionValue]
	)

	String actualDate = WebUI.getAttribute(
		dateInput,
		'value'
	)?.toString()?.trim() ?: ''

	String actualDescription = WebUI.getAttribute(
		descriptionInput,
		'value'
	)?.toString() ?: ''

	if (actualDate != visitDateValue) {
		KeywordUtil.markFailedAndStop(
			"Visit date input mismatch. " +
			"Expected='${visitDateValue}', actual='${actualDate}'"
		)
	}

	if (actualDescription != descriptionValue) {
		KeywordUtil.markFailedAndStop(
			"Description input mismatch. " +
			"Expected='${descriptionValue}', actual='${actualDescription}'"
		)
	}

	WebUI.executeJavaScript(
		"""
		var button = Array.from(
			document.querySelectorAll(
				"button[type='submit'], input[type='submit']"
			)
		).find(function(element) {
			return (element.innerText || element.value || '')
				.indexOf('Add Visit') >= 0;
		});

		if (!button) {
			throw new Error('Add Visit button was not found');
		}

		var form = button.closest('form');

		if (!form) {
			throw new Error('Visit form was not found');
		}

		HTMLFormElement.prototype.submit.call(form);
		""",
		null
	)

	WebUI.waitForPageLoad(30)

	String currentUrl = WebUI.getUrl()

	String pageText = WebUI.executeJavaScript(
		'return document.body ? document.body.innerText : "";',
		null
	)?.toString() ?: ''

	String fieldMessage = WebUI.executeJavaScript(
		"""
		var field = document.getElementById(arguments[0]);

		if (!field) {
			return '';
		}

		var container = field.closest('.form-group');

		if (!container) {
			container = field.parentElement;
		}

		return container ? container.innerText : '';
		""",
		[invalidField]
	)?.toString() ?: ''

	boolean stayedOnVisitPage =
		currentUrl.contains(
			"/owners/${ownerId}/pets/${petId}/visits/new"
		)

	boolean errorDisplayed =
		fieldMessage.toLowerCase().contains(
			expectedErrorMessage.toLowerCase()
		)

	if (!errorDisplayed) {
		errorDisplayed = pageText.toLowerCase().contains(
			expectedErrorMessage.toLowerCase()
		)
	}

	if (!stayedOnVisitPage || !errorDisplayed) {
		KeywordUtil.markFailedAndStop(
			"Expected validation message '${expectedErrorMessage}' " +
			"was not displayed. ID=${testId}, " +
			"invalid field=${invalidField}, URL=${currentUrl}, " +
			"field message=${fieldMessage}, page text=${pageText}"
		)
	}

	WebUI.comment(
		"PASSED ${testId}: ${caseName} | " +
		"Expected result=${expectedResult}"
	)
} catch (Throwable error) {
	WebUI.takeScreenshot()

	KeywordUtil.markFailed(
		"FAILED ${testId}: ${caseName}. " +
		"Reason: ${error.message}"
	)

	throw error
} finally {
	WebUI.closeBrowser()
}
