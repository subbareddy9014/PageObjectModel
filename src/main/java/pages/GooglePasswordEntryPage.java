package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GooglePasswordEntryPage extends Page {

    @FindBy(xpath = "//*[text()=\"Welcome\"]")
    private WebElement welcomeText;
    @FindBy(xpath = "//*[@id=\"password\"]//input")
    private WebElement passwordField;
    @FindBy(xpath = "//*[text()='Forgot password?']")
    private WebElement forgotPasswordTextLink;
    @FindBy(id = "passwordNext")
    private WebElement nextButton;
    @FindBy(xpath = "//*[text()='Wrong password. Try again or click Forgot password to reset it.']")
    private WebElement wrongPasswordText;

    public GooglePasswordEntryPage(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean enterPassword(String password) {
        waitForSomeTime(5);
        if (waitForElementVisibleInDOM(passwordField, 5)) {
            passwordField.clear();
            passwordField.sendKeys(password);
            waitForElementClickableInDOM(nextButton, 5);
            nextButton.click();
            return !waitForElementVisibleInDOM(wrongPasswordText, 3);
        }
        return false;
    }
}
