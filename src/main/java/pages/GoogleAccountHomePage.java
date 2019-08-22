package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GoogleAccountHomePage extends Page {

    @FindBy(xpath = "//*[text()='Home']")
    private WebElement home;
    @FindBy(xpath = "//*[text()='Manage your info, privacy and security to make Google work better for you']")
    private WebElement manageInfoText;
    @FindBy(xpath = "//*[@id=\"gb\"]//span[@class='gb_Ba gbii']")
    private WebElement profileIcon;
    @FindBy(xpath = "//*[text()='Sign out']")
    private WebElement signOutText;
    @FindBy(id = "sdgBod")
    private WebElement googleAccountImage;

    public GoogleAccountHomePage(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean validateLogin() {
        waitForSomeTime(10);
        return waitForElementVisibleInDOM(googleAccountImage, 10);
    }

    public void signOut() {
        waitForSomeTime(10);
        waitForElementClickableInDOM(profileIcon, 10);
        profileIcon.click();
        waitForElementClickableInDOM(signOutText, 10);
        signOutText.click();
    }
}
