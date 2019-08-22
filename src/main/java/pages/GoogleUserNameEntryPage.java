package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GoogleUserNameEntryPage extends Page {

    @FindBy(id = "identifierId")
    private WebElement userNameFiled;
    @FindBy(xpath = "//*[text()='Forgot email?']")
    private WebElement forgotEmailTextLink;
    @FindBy(xpath = "//*[text()='Create account']")
    private WebElement createNewAccountLink;
    @FindBy(id = "identifierNext")
    private WebElement nextButton;
    @FindBy(xpath = "//*[text()=\"Couldn't find your Google Account\"]")
    private WebElement couldNotFindAccountText;

    public GoogleUserNameEntryPage(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean enterUserName(String userName) {
        waitForSomeTime(5);
        if (waitForElementVisibleInDOM(userNameFiled, 10)) {
            userNameFiled.clear();
            userNameFiled.sendKeys(userName);
            waitForElementClickableInDOM(nextButton, 5);
            nextButton.click();
            return !waitForElementVisibleInDOM(couldNotFindAccountText, 3);
        }
        return false;
    }

}
