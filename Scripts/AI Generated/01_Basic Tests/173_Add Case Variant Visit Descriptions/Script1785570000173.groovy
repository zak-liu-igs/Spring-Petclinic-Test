import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String visitUrl = 'http://localhost:8080/owners/6/pets/7/visits/new'
String expectedOwnerUrl = 'http://localhost:8080/owners/6'
String stamp = System.currentTimeMillis().toString()
String upperDescription = ('CaseVariant' + stamp).toUpperCase()
String lowerDescription = upperDescription.toLowerCase()

TestObject descriptionInput = new TestObject('descriptionInput')
descriptionInput.addProperty('xpath', ConditionType.EQUALS, "//input[@id='description' and @name='description']")

TestObject addVisitButton = new TestObject('addVisitButton')
addVisitButton.addProperty('xpath', ConditionType.EQUALS,
    "//form[.//input[@id='description']]//button[@type='submit' and normalize-space(.)='Add Visit']")

def visitDescriptionCell = { String description ->
    TestObject object = new TestObject('visitDescription_' + description)
    object.addProperty('xpath', ConditionType.EQUALS,
        "//table[.//th[normalize-space(.)='Visit Date'] and .//th[normalize-space(.)='Description']]//td[normalize-space(.)='" + description + "']")
    return object
}

def normalizeUrl = { String url ->
    url.replaceFirst(';jsessionid=[^/?#]+', '')
}

def addVisit = { String description ->
    WebUI.navigateToUrl(visitUrl)
    WebUI.waitForPageLoad(10)
    WebUI.waitForElementVisible(descriptionInput, 10)
    WebUI.setText(descriptionInput, description)
    WebUI.click(addVisitButton)
    WebUI.waitForPageLoad(10)

    // PetClinic may append an optional ;jsessionid=... path parameter after redirect.
    // Normalize it before validating the stable owner details route.
    String normalizedUrl = normalizeUrl(WebUI.getUrl())
    WebUI.verifyMatch(normalizedUrl, '^' + expectedOwnerUrl + '/?$', true)
    WebUI.waitForElementVisible(visitDescriptionCell(description), 10)
    WebUI.verifyElementText(visitDescriptionCell(description), description)
}

try {
    WebUI.openBrowser('')

    WebUI.verifyNotEqual(upperDescription, lowerDescription)

    addVisit(upperDescription)
    addVisit(lowerDescription)

    WebUI.verifyElementText(visitDescriptionCell(upperDescription), upperDescription)
    WebUI.verifyElementText(visitDescriptionCell(lowerDescription), lowerDescription)
} finally {
    WebUI.closeBrowser()
}
