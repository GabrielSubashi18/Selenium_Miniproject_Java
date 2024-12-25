package Miniproject;

import org.example.Pages.HomePage;
import org.example.Pages.WomenPage;
import org.example.Pages.JacketPage;
import org.example.Pages.SignInPage;
import org.example.Utils.ExtentReportUtil;
import org.example.Utils.FileUtils;
import org.example.Utils.ScreenshotTestWatcher;
import org.example.Utils.WebDriverManagerUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.logging.Logger;

public class PageFiltersTest extends WebDriverManagerUtil {
    private static ExtentReportUtil extentReportUtil;
    private static ExtentReports report;
    private static ExtentTest test;
    private static Logger logger;

    @Rule
    public ScreenshotTestWatcher screenshotTestWatcher = new ScreenshotTestWatcher(driver);

    @BeforeClass
    public static void setupReport() {
        extentReportUtil = new ExtentReportUtil("reports/PageFiltersTestReport.html");
        report = extentReportUtil.getReport();
        test = extentReportUtil.getTest();
        logger = Logger.getLogger(PageFiltersTest.class.getName());
    }

    @AfterClass
    public static void tearDownReport() {
        extentReportUtil.flushReport();
    }

    @Test
    public void testCheckPageFilters() {
        extentReportUtil.createTest("Page Filters Test");
        try {
            // Read email and password
            String email = FileUtils.readFromFile("email.txt");
            String password = "Password123!";

            // Navigate to the website
            driver.get("https://magento.softwaretestingboard.com/");
            extentReportUtil.logInfo("Navigated to the home page.");

            // Login process
            HomePage homePage = new HomePage(driver);
            homePage.clickSignIn();
            extentReportUtil.logInfo("Clicked on 'Sign In'.");

            SignInPage signInPage = new SignInPage(driver);
            signInPage.login(email, password);
            extentReportUtil.logInfo("Logged in successfully.");

            // Navigate to Jackets via Women -> Tops -> Jackets
            WomenPage womenPage = new WomenPage(driver);
            womenPage.navigateToJackets();
            extentReportUtil.logInfo("Navigated to Women -> Tops -> Jackets.");

            // Apply filters and validate products
            JacketPage jacketPage = new JacketPage(driver, logger, test, report);

            // Apply and verify color filter
            jacketPage.applyColorFilter();
            extentReportUtil.logInfo("Applied 'Red' color filter.");
            jacketPage.verifyProductsHaveColor("red");
            extentReportUtil.logPass("Verified all products match the selected 'Red' color.");

            // Apply and verify price filter
            try {
                jacketPage.applyPriceFilter(50.00, 59.99);
                extentReportUtil.logInfo("Applied price filter '$50.00 - $59.99'.");
                extentReportUtil.logPass("Verified all products match the selected price range '$50.00 - $59.99'.");
            } catch (Exception e) {
                extentReportUtil.logFail("Test failed: " + e.getMessage());
                extentReportUtil.captureScreenshot(driver, "PriceFilterTestFailure");
                throw e;
            }
        } catch (Exception e) {
            extentReportUtil.logFail("Test failed: " + e.getMessage());
            extentReportUtil.captureScreenshot(driver, "PageFiltersTestFailure");
            throw e;
        } finally {
            if (driver != null) {
                driver.quit();
                extentReportUtil.logInfo("Closed the browser.");
            }
        }
    }
}