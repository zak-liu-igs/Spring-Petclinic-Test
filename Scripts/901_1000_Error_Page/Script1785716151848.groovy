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
	'expected_title',
	'expected_message',
	'match_mode',
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
String expectedTitle = data['expected_title']?.toString()?.trim() ?: ''
String expectedMessage = data['expected_message']?.toString()?.trim() ?: ''
String matchMode = data['match_mode']?.toString()?.trim() ?: ''
String scenarioType = data['scenario_type']?.toString()?.trim() ?: ''
String expectedResult = data['expected_result']?.toString()?.trim() ?: ''

if (!testId || !caseName || !targetUrl ||
	!expectedTitle || !expectedMessage ||
	!matchMode || !scenarioType || !expectedResult) {

	KeywordUtil.markFailedAndStop(
		"CSV row contains incomplete data. " +
		"id=${testId}, case=${caseName}, url=${targetUrl}, " +
		"expected_title=${expectedTitle}, " +
		"expected_message=${expectedMessage}"
	)
}

KeywordUtil.logInfo(
	"Running ${caseName} (${testId}) | " +
	"URL=${targetUrl} | scenario=${scenarioType}"
)

WebUI.openBrowser('')

try {
	WebUI.navigateToUrl(targetUrl)
	WebUI.waitForPageLoad(30)

	String currentUrl = WebUI.getUrl()

	String pageText = WebUI.executeJavaScript(
		'return document.body ? document.body.innerText : "";',
		null
	)?.toString() ?: ''

	String normalizedPageText = pageText.toLowerCase()
	String normalizedTitle = expectedTitle.toLowerCase()
	String normalizedMessage = expectedMessage.toLowerCase()

	boolean titleMatched
	boolean messageMatched

	if (matchMode.equalsIgnoreCase('case_insensitive_contains')) {
		titleMatched = normalizedPageText.contains(normalizedTitle)
		messageMatched = normalizedPageText.contains(normalizedMessage)
	} else if (matchMode.equalsIgnoreCase('contains')) {
		titleMatched = pageText.contains(expectedTitle)
		messageMatched = pageText.contains(expectedMessage)
	} else {
		titleMatched = pageText.trim() == expectedTitle
		messageMatched = pageText.trim() == expectedMessage
	}

	boolean errorUrlMatched =
		currentUrl.contains('/oups')

	if (!errorUrlMatched ||
		!titleMatched ||
		!messageMatched) {

		KeywordUtil.markFailedAndStop(
			"Error page validation failed. " +
			"ID=${testId}, URL=${currentUrl}, " +
			"expected title='${expectedTitle}', " +
			"expected message='${expectedMessage}', " +
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
