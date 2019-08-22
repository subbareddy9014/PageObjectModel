package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.TestConstants;

import java.util.concurrent.TimeUnit;

class Page {

    private static WebDriver driver;

    Page(WebDriver webDriver) {
        Page.driver = webDriver;
        PageFactory.initElements(Page.driver, this);
    }

    /**
     * Method to wait for element visibility
     *
     * @param element - Element for which we need to wait for
     * @param maxTime - time to wait in seconds
     * @return - status of element visibility
     */
    boolean waitForElementVisibleInDOM(WebElement element, int maxTime) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, maxTime);
        try {
            return null != webDriverWait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Method to wait for element clickable
     *
     * @param element - Element for which we need to wait for
     * @param maxTime - time to wait in seconds
     * @return - status of element visibility
     */
    boolean waitForElementClickableInDOM(WebElement element, int maxTime) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, maxTime);
        try {
            return null != webDriverWait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Method to pause execution
     *
     * @param waitTime - wit time in seconds
     */
    void waitForSomeTime(int waitTime) {
        driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
        try {
            driver.findElement(By.id(""));
        } catch (NoSuchElementException ignored) {
        }
        turnOnImplicitWait();
    }

    /**
     * Method to set default implicit wait on driver
     */
    private void turnOnImplicitWait() {
        driver.manage().timeouts().implicitlyWait(TestConstants.DRIVER_IMPLICIT_WAIT, TimeUnit.SECONDS);
    }
}
