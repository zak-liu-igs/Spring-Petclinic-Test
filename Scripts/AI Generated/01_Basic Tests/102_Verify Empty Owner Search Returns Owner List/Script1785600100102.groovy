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
    WebUI.navigateToUrl(baseUrl + '/owners/find')
    WebUI.setText(findTestObject('Page_PetClinic  a Spring Framework demonstration/input_lastName'), '')
    WebUI.click(findTestObject('Page_PetClinic  a Spring Framework demonstration/button_Find Owner'))

    WebUI.verifyElementPresent(xpath('ownersHeading', "//h2[normalize-space(.)='Owners']"), 10)
    List ownerRows = WebUI.findWebElements(xpath('ownerRows',
        "//table[@id='owners']//tbody/tr | //table[contains(@class,'table-striped')]//tbody/tr"), 10)
    WebUI.verifyEqual(ownerRows.size() > 0, true)
    WebUI.verifyMatch(WebUI.getUrl(), '.*/owners(?:\\?.*)?$', true)
} finally {
    WebUI.closeBrowser()
}
