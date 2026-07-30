import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String baseUrl = 'http://localhost:8080'

def xpath = { String name, String expression ->
    TestObject object = new TestObject(name)
    object.addProperty('xpath', ConditionType.EQUALS, expression)
    object
}

try {
    WebUI.openBrowser('')
    WebUI.navigateToUrl(baseUrl + '/owners/new')

    ['First Name', 'Last Name', 'Address', 'City', 'Telephone'].each { String label ->
        WebUI.verifyElementPresent(xpath('label' + label,
            "//label[normalize-space(.)='" + label + "']"), 10)
    }

    ['firstName', 'lastName', 'address', 'city', 'telephone'].each { String fieldId ->
        TestObject field = xpath('field' + fieldId, "//*[@id='" + fieldId + "']")
        WebUI.verifyElementAttributeValue(field, 'name', fieldId, 10)
        WebUI.verifyElementAttributeValue(field, 'type', 'text', 10)
    }
} finally {
    WebUI.closeBrowser()
}
