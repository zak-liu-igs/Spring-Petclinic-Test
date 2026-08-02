import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

WebUI.comment("Running ${id}: ${case_name}")
WebUI.openBrowser('')

try {
    WebUI.navigateToUrl(url.toString().trim())
    WebUI.waitForPageLoad(10)

    String actualTitle = WebUI.getWindowTitle()
    WebUI.verifyMatch(actualTitle, expected_title.toString().trim(), false)

    String homeText = expected_home_text.toString().trim()
    WebUI.verifyTextPresent("(?i)${java.util.regex.Pattern.quote(homeText)}", true)
} finally {
    WebUI.closeBrowser()
}
