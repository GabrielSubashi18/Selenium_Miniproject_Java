package Miniproject;

import org.example.Pages.HomePage;
import org.example.Pages.SignInPage;
import org.example.Utils.ExtentReportUtil;
import org.example.Utils.ScreenshotTestWatcher;
import org.example.Utils.WebDriverManagerUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;

import static org.junit.Assert.assertTrue;

public class SignInTest extends WebDriverManagerUtil {
    private static ExtentReportUtil extentReportUtil;

    @Rule
    public ScreenshotTestWatcher screenshotTestWatcher = new ScreenshotTestWatcher(driver);

    @BeforeClass
    public static void setupReport() {
        extentReportUtil = new ExtentReportUtil("reports/SignInTestReport.html");
    }

    @AfterClass
    public static void tearDownReport() {
        extentReportUtil.flushReport();
    }

    @Test
    public void testSignIn() {
        extentReportUtil.createTest("Sign In Test");
        try {
            String email = "john.doe@gmail.com"; // Replace with actual email
            String password = "Password123!"; // Replace with actual password

            driver.get("https://magento.softwaretestingboard.com/");
            extentReportUtil.logInfo("Navigated to the home page.");

            // Sign in process
            HomePage homePage = new HomePage(driver);
            homePage.clickSignIn();
            extentReportUtil.logInfo("Clicked on 'Sign In'.");

            SignInPage signInPage = new SignInPage(driver);
            signInPage.login(email, password);
            extentReportUtil.logInfo("Entered email and password, clicked 'Sign In'.");

            assertTrue(signInPage.isWelcomeMessageDisplayed("Welcome, John Doe!"));
            extentReportUtil.logPass("Verified the welcome message is displayed.");

            homePage.clickUserProfileDropdown();
            extentReportUtil.logPass("Clicked on the user profile dropdown.");

            // Sign out
            homePage.signOut();
            extentReportUtil.logInfo("Signed out successfully.");
        } catch (Exception e) {
            extentReportUtil.logFail("Test failed: " + e.getMessage());
            extentReportUtil.captureScreenshot(driver, "SignInTestFailure");
            throw e; // Ensure the test is marked as failed
        }
    }
}
