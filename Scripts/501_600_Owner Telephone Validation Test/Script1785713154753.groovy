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
	'first_name',
	'last_name',
	'address',
	'city',
	'telephone',
	'expected_valid',
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
String firstNameValue = data['first_name']?.toString()?.trim() ?: ''
String lastNameValue = data['last_name']?.toString()?.trim() ?: ''
String addressValue = data['address']?.toString()?.trim() ?: ''
String cityValue = data['city']?.toString()?.trim() ?: ''
String telephoneValue = data['telephone']?.toString()?.trim() ?: ''
String expectedValidValue = data['expected_valid']?.toString()?.trim() ?: ''
String expectedErrorKey = data['expected_error_key']?.toString()?.trim() ?: ''
String scenarioType = data['scenario_type']?.toString()?.trim() ?: ''
String expectedResult = data['expected_result']?.toString()?.trim() ?: ''

if (!testId || !caseName || !targetUrl ||
	!firstNameValue || !lastNameValue ||
	!addressValue || !cityValue || !expectedValidValue) {

	KeywordUtil.markFailedAndStop(
		"CSV row contains incomplete data. " +
		"id=${testId}, case_name=${caseName}, url=${targetUrl}, " +
		"first_name=${firstNameValue}, last_name=${lastNameValue}, " +
		"address=${addressValue}, city=${cityValue}, " +
		"expected_valid=${expectedValidValue}"
	)
}

boolean shouldBeValid = expectedValidValue.equalsIgnoreCase('true')

Map<String, String> errorMessages = [
	'required'          : 'must not be blank',
	'telephone.invalid' : 'Telephone must be a 10-digit number'
]

TestObject firstNameInput = new TestObject('firstNameInput')
firstNameInput.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[@id='firstName' or @name='firstName']"
)

TestObject lastNameInput = new TestObject('lastNameInput')
lastNameInput.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[@id='lastName' or @name='lastName']"
)

TestObject addressInput = new TestObject('addressInput')
addressInput.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[@id='address' or @name='address']"
)

TestObject cityInput = new TestObject('cityInput')
cityInput.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[@id='city' or @name='city']"
)

TestObject telephoneInput = new TestObject('telephoneInput')
telephoneInput.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[@id='telephone' or @name='telephone']"
)

TestObject submitButton = new TestObject('submitButton')
submitButton.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//button[@type='submit' and contains(normalize-space(.), 'Add Owner')] | " +
	"//input[@type='submit']"
)

KeywordUtil.logInfo(
	"Running ${caseName} (${testId}) | " +
	"telephone='${telephoneValue}' | " +
	"expectedValid=${shouldBeValid} | " +
	"scenario=${scenarioType}"
)

WebUI.openBrowser('')

try {
	WebUI.navigateToUrl(targetUrl)
	WebUI.waitForPageLoad(30)

	WebUI.waitForElementVisible(firstNameInput, 10)
	WebUI.waitForElementVisible(lastNameInput, 10)
	WebUI.waitForElementVisible(addressInput, 10)
	WebUI.waitForElementVisible(cityInput, 10)
	WebUI.waitForElementVisible(telephoneInput, 10)
	WebUI.waitForElementVisible(submitButton, 10)

	WebUI.clearText(firstNameInput)
	WebUI.setText(firstNameInput, firstNameValue)

	WebUI.clearText(lastNameInput)
	WebUI.setText(lastNameInput, lastNameValue)

	WebUI.clearText(addressInput)
	WebUI.setText(addressInput, addressValue)

	WebUI.clearText(cityInput)
	WebUI.setText(cityInput, cityValue)

	WebUI.clearText(telephoneInput)

	if (telephoneValue) {
		WebUI.setText(telephoneInput, telephoneValue)
	}

	String actualTelephone = WebUI.getAttribute(
		telephoneInput,
		'value'
	)?.toString() ?: ''

	if (actualTelephone != telephoneValue) {
		KeywordUtil.markFailedAndStop(
			"Telephone input mismatch. " +
			"Expected='${telephoneValue}', actual='${actualTelephone}'"
		)
	}

	WebUI.click(submitButton)
	WebUI.waitForPageLoad(30)

	String currentUrl = WebUI.getUrl()

	String pageText = WebUI.executeJavaScript(
		'return document.body ? document.body.innerText : "";',
		null
	)?.toString() ?: ''

	if (shouldBeValid) {
		boolean redirectedToOwnerPage =
			currentUrl ==~ /.*\/owners\/\d+\/?/

		boolean firstNameDisplayed =
			pageText.toLowerCase().contains(
				firstNameValue.toLowerCase()
			)

		boolean lastNameDisplayed =
			pageText.toLowerCase().contains(
				lastNameValue.toLowerCase()
			)

		boolean telephoneDisplayed =
			pageText.contains(telephoneValue)

		if (!redirectedToOwnerPage ||
			!firstNameDisplayed ||
			!lastNameDisplayed ||
			!telephoneDisplayed) {

			KeywordUtil.markFailedAndStop(
				"Expected successful owner creation. " +
				"ID=${testId}, telephone=${telephoneValue}, " +
				"URL=${currentUrl}, page text=${pageText}"
			)
		}
	} else {
		String expectedErrorMessage =
			errorMessages[expectedErrorKey] ?: expectedErrorKey

		if (!expectedErrorMessage) {
			KeywordUtil.markFailedAndStop(
				"No expected error message configured. " +
				"ID=${testId}, error key=${expectedErrorKey}"
			)
		}

		boolean stayedOnNewOwnerPage =
			currentUrl.contains('/owners/new')

		boolean errorDisplayed =
			pageText.toLowerCase().contains(
				expectedErrorMessage.toLowerCase()
			)

		if (!stayedOnNewOwnerPage || !errorDisplayed) {
			KeywordUtil.markFailedAndStop(
				"Expected validation message '${expectedErrorMessage}' " +
				"was not displayed. ID=${testId}, " +
				"telephone='${telephoneValue}', URL=${currentUrl}, " +
				"page text=${pageText}"
			)
		}
	}

	WebUI.comment(
		"PASSED ${testId}: ${caseName} | " +
		"Expected result=${expectedResult}"
	)
} catch (Throwable error) {
	WebUI.takeScreenshot()
	throw error
} finally {
	WebUI.closeBrowser()
}