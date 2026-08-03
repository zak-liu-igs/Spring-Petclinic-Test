import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Map<String, Object> data = [:]

binding.variables.each { key, value ->
	String normalizedKey = key.toString()
		.replace('\\n', '')
		.replace('\\r', '')
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
	'pet_name',
	'birth_date',
	'pet_type',
	'expected_text',
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
String petNameValue = data['pet_name']?.toString()?.trim() ?: ''
String birthDateValue = data['birth_date']?.toString()?.trim() ?: ''
String petTypeValue = data['pet_type']?.toString()?.trim() ?: ''
String expectedTextValue = data['expected_text']?.toString()?.trim() ?: ''
String scenarioType = data['scenario_type']?.toString()?.trim() ?: ''
String expectedResult = data['expected_result']?.toString()?.trim() ?: ''

if (!testId || !caseName || !targetUrl || !ownerId || !petId ||
	!petNameValue || !birthDateValue || !petTypeValue ||
	!expectedTextValue) {

	KeywordUtil.markFailedAndStop(
		"CSV row contains incomplete data. " +
		"id=${testId}, case=${caseName}, url=${targetUrl}, " +
		"owner_id=${ownerId}, pet_id=${petId}, " +
		"pet_name=${petNameValue}, birth_date=${birthDateValue}, " +
		"pet_type=${petTypeValue}"
	)
}

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

TestObject updatePetButton = new TestObject('updatePetButton')
updatePetButton.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//button[@type='submit' and contains(normalize-space(.), 'Update Pet')] | " +
	"//input[@type='submit']"
)

KeywordUtil.logInfo(
	"Running ${caseName} (${testId}) | " +
	"owner=${ownerId} | pet=${petId} | " +
	"name=${petNameValue} | type=${petTypeValue} | " +
	"scenario=${scenarioType}"
)

WebUI.openBrowser('')

try {
	WebUI.navigateToUrl(targetUrl)
	WebUI.waitForPageLoad(30)

	WebUI.waitForElementVisible(petNameInput, 10)
	WebUI.waitForElementVisible(birthDateInput, 10)
	WebUI.waitForElementVisible(petTypeSelect, 10)
	WebUI.waitForElementVisible(updatePetButton, 10)

	WebUI.clearText(petNameInput)
	WebUI.setText(petNameInput, petNameValue)

	WebUI.executeJavaScript(
		"""
		var field = document.getElementById('birthDate');

		if (!field) {
			throw new Error('birthDate field was not found');
		}

		field.value = arguments[0];
		field.dispatchEvent(new Event('input', { bubbles: true }));
		field.dispatchEvent(new Event('change', { bubbles: true }));
		""",
		[birthDateValue]
	)

	WebUI.selectOptionByLabel(
		petTypeSelect,
		petTypeValue,
		false
	)

	String actualPetName = WebUI.getAttribute(
		petNameInput,
		'value'
	)?.toString()?.trim() ?: ''

	String actualBirthDate = WebUI.getAttribute(
		birthDateInput,
		'value'
	)?.toString()?.trim() ?: ''

	String actualPetType = WebUI.executeJavaScript(
		"""
		var select = document.getElementById('type');

		if (!select || select.selectedIndex < 0) {
			return '';
		}

		return select.options[select.selectedIndex].text;
		""",
		null
	)?.toString()?.trim() ?: ''

	if (actualPetName != petNameValue) {
		KeywordUtil.markFailedAndStop(
			"Pet name input mismatch. " +
			"Expected='${petNameValue}', actual='${actualPetName}'"
		)
	}

	if (actualBirthDate != birthDateValue) {
		KeywordUtil.markFailedAndStop(
			"Birth date input mismatch. " +
			"Expected='${birthDateValue}', actual='${actualBirthDate}'"
		)
	}

	if (!actualPetType.equalsIgnoreCase(petTypeValue)) {
		KeywordUtil.markFailedAndStop(
			"Pet type input mismatch. " +
			"Expected='${petTypeValue}', actual='${actualPetType}'"
		)
	}

	WebUI.click(updatePetButton)
	WebUI.waitForPageLoad(30)

	String currentUrl = WebUI.getUrl()

	String pageText = WebUI.executeJavaScript(
		'return document.body ? document.body.innerText : "";',
		null
	)?.toString() ?: ''

	boolean redirectedToOwnerPage =
		currentUrl.contains("/owners/${ownerId}")

	boolean petNameDisplayed =
		pageText.toLowerCase().contains(
			expectedTextValue.toLowerCase()
		)

	boolean petTypeDisplayed =
		pageText.toLowerCase().contains(
			petTypeValue.toLowerCase()
		)

	if (!redirectedToOwnerPage ||
		!petNameDisplayed ||
		!petTypeDisplayed) {

		KeywordUtil.markFailedAndStop(
			"Pet update was not verified. " +
			"ID=${testId}, expected name=${expectedTextValue}, " +
			"expected type=${petTypeValue}, URL=${currentUrl}, " +
			"page text=${pageText}"
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