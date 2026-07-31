import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

Map<String, String> labelsByField = [
    'firstName': 'First Name',
    'lastName': 'Last Name',
    'address': 'Address',
    'city': 'City',
    'telephone': 'Telephone'
]

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')

    labelsByField.each { String fieldId, String labelText ->
        WebUI.verifyElementPresent(xpath('labelFor' + fieldId,
            "//form[@id='add-owner-form']//label[@for='" + fieldId +
            "' and normalize-space(.)='" + labelText + "']"), 10)
        WebUI.verifyElementPresent(xpath('inputFor' + fieldId,
            "//form[@id='add-owner-form']//*[@id='" + fieldId + "' and @name='" + fieldId + "']"), 10)
    }
} finally {
    WebUI.closeBrowser()
}
