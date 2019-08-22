package utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import drivers.DriversFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArchUtils;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class BaseTest implements ITestListener {

    private static WebDriver driver;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss-SSS");
    private static final String EXTENT_REPORT_FOLDER = "./build/reports/ExtentReport/";
    private static ExtentHtmlReporter extentHtmlReporter;
    private static ExtentReports extentReports;
    private static ExtentTest extentTest;
    private static DriversFactory driversFactory;

    protected GoogleUserNameEntryPage googleUserNameEntryPage = null;
    protected GooglePasswordEntryPage googlePasswordEntryPage = null;
    protected GoogleAccountHomePage googleAccountHomePage = null;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"browser"})
    public void initializeTestEnvironment(@Optional("") String browser) {
        TestConstants.BROWSER = browser.length() == 0 ? TestConstants.BROWSER : browser;
        switch (TestConstants.BROWSER.toLowerCase()) {
            case "chrome":
                if (SystemUtils.IS_OS_LINUX && ArchUtils.getProcessor().is32Bit()) {
                    Util.showMessage("On 32 bit linux machine, can't run test cases in chrome browser ", "warning");
                    return;
                }
                break;
            case "ie":
                if (!SystemUtils.IS_OS_WINDOWS) {
                    Util.showMessage("Test cases on Internet Explorer can only be run on Windows machine", "warning");
                    return;
                }
                break;
            case "edge":
                if (!SystemUtils.IS_OS_WINDOWS) {
                    Util.showMessage("Test cases on Edge browser can only be run on Windows machine", "warning");
                    return;
                }
                break;
            case "safari":
                if (!SystemUtils.IS_OS_MAC) {
                    Util.showMessage("Test cases on Safari can only be run on Mac machine", "warning");
                    return;
                }
                break;
        }
        driversFactory = new DriversFactory(TestConstants.BROWSER);

        extentHtmlReporter = new ExtentHtmlReporter(EXTENT_REPORT_FOLDER + "index.html");
        extentHtmlReporter.config().setDocumentTitle("Gmail user accounts verification report");
        extentHtmlReporter.config().setReportName("Gmail User Accounts Test");
        extentHtmlReporter.config().setTheme(Theme.DARK);

        extentReports = new ExtentReports();
        extentReports.attachReporter(extentHtmlReporter);
        extentReports.setSystemInfo("Test Machine User Name", System.getProperty("user.name", "Git Hub"));
        extentReports.setSystemInfo("Test Machine OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Browser used for tests", TestConstants.BROWSER);
    }

    /**
     * Method will execute before every test method.
     */
    @BeforeMethod(alwaysRun = true)
    public void navigateToStartingPage() {
        try {
            driver = driversFactory.getDriver();
            driver.manage().timeouts().implicitlyWait(TestConstants.DRIVER_IMPLICIT_WAIT, TimeUnit.SECONDS);
        } catch (IOException e) {
            Util.showMessage(e.getMessage(), "error");
        }
        driver.manage().window().maximize();
        driver.get(TestConstants.GMAIL_URL);
        googleUserNameEntryPage = new GoogleUserNameEntryPage(driver);
        googlePasswordEntryPage = new GooglePasswordEntryPage(driver);
        googleAccountHomePage = new GoogleAccountHomePage(driver);
    }

    /**
     * Method will execute after every test method.
     */
    @AfterMethod(alwaysRun = true)
    public void runAfterTest() {
        driver.close();
        driver.quit();
    }


    /**
     * Method will execute once all the test cases in the suite are completed.
     */
    @AfterSuite(alwaysRun = true)
    public void runAfterSuite() {
        extentReports.flush();
        extentHtmlReporter = null;
        extentReports = null;
    }


    @Override
    public void onTestStart(ITestResult result) {
        extentTest = extentReports.createTest(result.getMethod().getMethodName() + " Test");
        extentTest.log(Status.INFO, "Test " + result.getInstanceName() + " " + result.getMethod().getMethodName() + " Started");
        extentTest.log(Status.INFO, "Login with User Name : " + result.getParameters()[0] + " Password : " + result.getParameters()[1]);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.log(Status.PASS, "User Name : " + result.getParameters()[0] + " Password : " + result.getParameters()[1] + " are working fine");
        captureBrowserConsoleLog();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String screenshot = LocalTime.now().format(formatter);
        try {
            captureScreenshot(screenshot);
            extentTest.log(Status.FAIL, "User Name : " + result.getParameters()[0] + " Password : " + result.getParameters()[1] + " are not working\n" +
                    extentTest.addScreenCaptureFromPath("./Screenshots/" + screenshot + ".JPEG") + "\n" + result.getThrowable() + "\n");
            captureBrowserConsoleLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.log(Status.WARNING, "Skipped : " + result.getMethod().getMethodName() + " due to \n" + result.getThrowable() + "\n");
        captureBrowserConsoleLog();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext result) {
    }

    @Override
    public void onFinish(ITestContext result) {
        /*Set<ITestResult> failedTests = result.getFailedTests().getAllResults();
        Set<ITestResult> skippedTests = result.getSkippedTests().getAllResults();
        Set<ITestResult> passedTests = result.getPassedTests().getAllResults();

        //Removing duplicates in individual results
        failedTests.removeIf(failedTest -> (result.getFailedTests().getResults(failedTest.getMethod()).size() > 1));
        skippedTests.removeIf(skippedTest -> (result.getSkippedTests().getResults(skippedTest.getMethod()).size() > 1));
        passedTests.removeIf(passedTest -> (result.getPassedTests().getResults(passedTest.getMethod()).size() > 1));

        //Removing duplicates across the results
        skippedTests.removeIf(skippedTest -> (result.getFailedTests().getResults(skippedTest.getMethod()).size() > 0));
        skippedTests.removeIf(skippedTest -> (result.getPassedTests().getResults(skippedTest.getMethod()).size() > 0));*/
    }

    private void captureBrowserConsoleLog() {
        extentTest.log(Status.INFO, "Please find out the log below");
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logEntries) {
            extentTest.log(Status.INFO, (entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
        }
    }

    /**
     * Method to take screenshot.
     *
     * @param fileName - screen shot name
     */
    private void captureScreenshot(String fileName) {
        try {
            File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, new File(EXTENT_REPORT_FOLDER + "Screenshots/" + fileName + ".JPEG"));
        } catch (NullPointerException | IOException ignored) {
        }
    }
}