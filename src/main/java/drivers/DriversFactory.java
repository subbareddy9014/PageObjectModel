package drivers;

import org.apache.commons.lang3.ArchUtils;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DriversFactory {

    private String os = SystemUtils.IS_OS_WINDOWS ? "windows" : SystemUtils.IS_OS_LINUX ? "linux" : "mac";
    private String bit = SystemUtils.IS_OS_MAC ? "" : ArchUtils.getProcessor().is32Bit() ? "32//" : "64//";
    private String browser;

    /**
     * @param browser - browser required.
     */
    public DriversFactory(String browser) {
        this.browser = browser;
    }

    public WebDriver getDriver() throws IOException {
        StringBuilder dir = new StringBuilder(String.format(new File(".").getCanonicalPath() + "//src//main//resources//drivers//%s//%s", os, bit));
        String driverFile = "";
        LoggingPreferences loggingPreferences = new LoggingPreferences();
        loggingPreferences.enable(LogType.BROWSER, Level.ALL);
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("disable-infobars");
                chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);
                driverFile = SystemUtils.IS_OS_WINDOWS ? "chromedriver.exe" : "chromedriver";
                System.setProperty("webdriver.chrome.driver", dir.append(driverFile).toString());
                return SystemUtils.IS_OS_LINUX ? ArchUtils.getProcessor().is32Bit() ? null :new ChromeDriver(chromeOptions) : new ChromeDriver(chromeOptions);
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);
                driverFile = SystemUtils.IS_OS_WINDOWS ? "geckodriver.ext" : "geckodriver";
                System.setProperty("webdriver.gecko.driver", dir.append(driverFile).toString());
                return new FirefoxDriver(firefoxOptions);
            case "ie":
                InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
                internetExplorerOptions.setCapability("ignoreZoomSetting", true);
                internetExplorerOptions.setCapability("ignoreProtectedModeSettings", true);
                internetExplorerOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                internetExplorerOptions.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);
                System.setProperty("webdriver.ie.driver", dir.append("IEDriverServer.exe").toString());
                return SystemUtils.IS_OS_WINDOWS ? new InternetExplorerDriver(internetExplorerOptions) : null;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);
                System.setProperty("webdriver.edge.driver", dir.append("msedgedriver.exe").toString());
                return SystemUtils.IS_OS_WINDOWS ? new EdgeDriver(edgeOptions) : null;
            case "safari":
                SafariOptions safariOptions = new SafariOptions();
                safariOptions.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);
                System.setProperty("webdriver.safari.noinstall", "true");
                return SystemUtils.IS_OS_MAC ? new SafariDriver(safariOptions) : null;
        }
        return null;
    }

}
