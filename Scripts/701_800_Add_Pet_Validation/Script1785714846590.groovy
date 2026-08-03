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
	'pet_name',
	'birth_date',
	'pet_type',
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
String petNameValue = data['pet_name']?.toString()?.trim() ?: ''
String birthDateValue = data['birth_date']?.toString()?.trim() ?: ''
String petTypeValue = data['pet_type']?.toString()?.trim() ?: ''
String invalidField = data['invalid_field']?.toString()?.trim() ?: ''
String expectedErrorKey = data['expected_error_key']?.toString()?.trim() ?: ''
String scenarioType = data['scenario_type']?.toString()?.trim() ?: ''
String expectedResult = data['expected_result']?.toString()?.trim() ?: ''

if (!testId || !caseName || !targetUrl || !ownerId ||
	!invalidField || !expectedErrorKey || !scenarioType ||
	!expectedResult) {

	KeywordUtil.markFailedAndStop(
		"CSV row contains incomplete data. " +
		"id=${testId}, case=${caseName}, url=${targetUrl}, " +
		"owner_id=${ownerId}, invalid_field=${invalidField}, " +
		"expected_error_key=${expectedErrorKey}"
	)
}

Map<String, String> errorMessages = [
	'required'     : 'is required',
	'invalid_date' : 'invalid date'
]

String expectedErrorMessage =
	errorMessages[expectedErrorKey] ?: expectedErrorKey

TestObject petNameInput = new TestObject('petNameInput')
petNameInput.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[@id='name' or @name='name']"
)

TestObject birthDateInput = new TestObject('birthDateInput')
birthDateInput.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[@id='birthDate' or @name='birthDate']"
)

TestObject petTypeSelect = new TestObject('petTypeSelect')
petTypeSelect.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//select[@id='type' or @name='type']"
)

TestObject addPetButton = new TestObject('addPetButton')
addPetButton.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//button[@type='submit' and contains(normalize-space(.), 'Add Pet')] | " +
	"//input[@type='submit']"
)

KeywordUtil.logInfo(
	"Running ${caseName} (${testId}) | " +
	"scenario=${scenarioType} | invalidField=${invalidField} | " +
	"expectedError=${expectedErrorMessage}"
)

WebUI.openBrowser('')

try {
	WebUI.navigateToUrl(targetUrl)
	WebUI.waitForPageLoad(30)

	WebUI.waitForElementVisible(petNameInput, 10)
	WebUI.waitForElementVisible(birthDateInput, 10)
	WebUI.waitForElementVisible(petTypeSelect, 10)
	WebUI.waitForElementVisible(addPetButton, 10)

	WebUI.clearText(petNameInput)

	if (petNameValue) {
		WebUI.setText(petNameInput, petNameValue)
	}

	WebUI.executeJavaScript(
		"""
		var field = document.getElementById('birthDate');

		if (!field) {
			throw new Error('birthDate field was not found');
		}

		field.value = arguments[0] || '';
		field.dispatchEvent(
			new Event('input', { bubbles: true })
		);
		field.dispatchEvent(
			new Event('change', { bubbles: true })
		);
		""",
		[birthDateValue]
	)

	if (petTypeValue) {
		WebUI.selectOptionByLabel(
			petTypeSelect,
			petTypeValue,
			false
		)
	} else {
		WebUI.executeJavaScript(
			"""
			var select = document.getElementById('type');

			if (!select) {
				throw new Error('Pet type field was not found');
			}

			select.value = '';

			if (select.value !== '') {
				select.selectedIndex = -1;
			}

			select.dispatchEvent(
				new Event('change', { bubbles: true })
			);
			""",
			null
		)
	}

	String actualPetName = WebUI.getAttribute(
		petNameInput,
		'value'
	)?.toString() ?: ''

	String actualBirthDate = WebUI.getAttribute(
		birthDateInput,
		'value'
	)?.toString() ?: ''

	if (actualPetName.trim() != petNameValue) {
		KeywordUtil.markFailedAndStop(
			"Pet name input mismatch. " +
			"Expected='${petNameValue}', actual='${actualPetName}'"
		)
	}

	if (actualBirthDate.trim() != birthDateValue) {
		KeywordUtil.markFailedAndStop(
			"Birth date input mismatch. " +
			"Expected='${birthDateValue}', actual='${actualBirthDate}'"
		)
	}

	WebUI.click(addPetButton)
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

		var group = field.closest('.form-group');

		if (!group) {
			group = field.parentElement;
		}

		return group ? group.innerText : '';
		""",
		[invalidField]
	)?.toString() ?: ''

	boolean stayedOnAddPetPage =
		currentUrl.contains("/owners/${ownerId}/pets/new")

	boolean errorDisplayed =
		fieldMessage.toLowerCase().contains(
			expectedErrorMessage.toLowerCase()
		)

	if (!errorDisplayed) {
		errorDisplayed = pageText.toLowerCase().contains(
			expectedErrorMessage.toLowerCase()
		)
	}

	if (!stayedOnAddPetPage || !errorDisplayed) {
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
