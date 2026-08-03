import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Map<String, Object> normalizedVariables = [:]

binding.variables.each { key, value ->
	String normalizedKey = key.toString().trim()
	normalizedVariables[normalizedKey] = value
}

List<String> requiredVariables = [
	'id',
	'case_name',
	'url',
	'last_name',
	'expected_text',
	'match_mode',
	'scenario_type',
	'expected_result'
]

List<String> missingVariables = requiredVariables.findAll {
	!normalizedVariables.containsKey(it)
}

if (!missingVariables.isEmpty()) {
	KeywordUtil.markFailedAndStop(
		"Missing CSV variables: ${missingVariables}. " +
		"Available variables: ${binding.variables.keySet()}"
	)
}

String testId = normalizedVariables['id']?.toString()?.trim()
String scenarioName = normalizedVariables['case_name']?.toString()?.trim()
String targetUrl = normalizedVariables['url']?.toString()?.trim()
String lastNameValue = normalizedVariables['last_name']?.toString()?.trim()
String expectedTextValue = normalizedVariables['expected_text']?.toString()?.trim()
String matchModeValue = normalizedVariables['match_mode']?.toString()?.trim()
String scenarioType = normalizedVariables['scenario_type']?.toString()?.trim()
String expectedResult = normalizedVariables['expected_result']?.toString()?.trim()

if (!testId || !scenarioName || !targetUrl ||
	!lastNameValue || !expectedTextValue) {
	KeywordUtil.markFailedAndStop(
		"CSV row contains empty data: " +
		"id=${testId}, case_name=${scenarioName}, url=${targetUrl}, " +
		"last_name=${lastNameValue}, expected_text=${expectedTextValue}"
	)
}

KeywordUtil.logInfo(
	"Running ${scenarioName} (${testId}) | " +
	"lastName=${lastNameValue} | scenario=${scenarioType}"
)

WebUI.openBrowser('')

try {
	WebUI.navigateToUrl(targetUrl)
	WebUI.waitForPageLoad(30)

	def lastNameObject = findTestObject(
		'Page_PetClinic  a Spring Framework demonstration/input_lastName'
	)

	def findOwnerButton = findTestObject(
		'Page_PetClinic  a Spring Framework demonstration/button_Find Owner'
	)

	WebUI.waitForElementVisible(lastNameObject, 10)
	WebUI.clearText(lastNameObject)
	WebUI.setText(lastNameObject, lastNameValue)

	String actualValue = WebUI.getAttribute(
		lastNameObject,
		'value'
	)?.toString()?.trim()

	WebUI.verifyMatch(actualValue, lastNameValue, false)

	WebUI.click(findOwnerButton)
	WebUI.waitForPageLoad(30)

	String pageText = WebUI.executeJavaScript(
		'return document.body ? document.body.innerText : "";',
		null
	)?.toString() ?: ''

	boolean matched = false

	if (matchModeValue.equalsIgnoreCase('case_insensitive_contains')) {
		matched = pageText.toLowerCase().contains(
			expectedTextValue.toLowerCase()
		)
	} else if (matchModeValue.equalsIgnoreCase('contains')) {
		matched = pageText.contains(expectedTextValue)
	} else {
		matched = pageText.trim() == expectedTextValue
	}

	if (!matched) {
		KeywordUtil.markFailedAndStop(
			"Expected text '${expectedTextValue}' was not found. " +
			"Test ID=${testId}, last name=${lastNameValue}, " +
			"URL=${WebUI.getUrl()}, page text=${pageText}"
		)
	}

	WebUI.comment(
		"PASSED ${testId}: ${scenarioName} | " +
		"Expected result=${expectedResult}"
	)
} catch (Throwable error) {
	WebUI.takeScreenshot()

	KeywordUtil.markFailed(
		"FAILED ${testId}: ${scenarioName}. Reason: ${error.message}"
	)

	throw error
} finally {
	WebUI.closeBrowser()
}