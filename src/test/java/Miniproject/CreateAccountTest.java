package Miniproject;
import org.junit.Rule;
import org.example.Pages.CreateAccountPage;
import org.example.Pages.HomePage;
import org.example.Utils.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.fail;

public class CreateAccountTest extends WebDriverManagerUtil {
    private static ExtentReportUtil extentReportUtil;
    @Rule
    public ScreenshotTestWatcher screenshotTestWatcher = new ScreenshotTestWatcher(driver);

    @BeforeClass
    public static void setupReport() {
        extentReportUtil = new ExtentReportUtil("reports/CreateAccountTestReport.html");
    }

    @AfterClass
    public static void tearDownReport() {
        extentReportUtil.flushReport();
    }

    @Test
    public void testCreateAccount() {
        extentReportUtil.createTest("Create Account Test");
        try {
            String firstName = "John";
            String lastName = "Doe";
            String email = "john.doe" + UUID.randomUUID() + "@gmail.com";
            String password = "Password123!";

            driver.get("https://magento.softwaretestingboard.com/");
            extentReportUtil.logInfo("Navigated to the home page.");

            // Navigate to Create Account page
            HomePage homePage = new HomePage(driver);
            homePage.clickCreateAccount();
            extentReportUtil.logInfo("Clicked on 'Create Account'.");

            // Fill out the Create Account form
            CreateAccountPage createAccountPage = new CreateAccountPage(driver);
            WaitUtils.waitForElementToBeVisible(driver, createAccountPage.getFirstNameField());
            createAccountPage.fillForm(firstName, lastName, email, password);
            extentReportUtil.logInfo("Filled the create account form.");
            createAccountPage.clickCreateAccount();
            extentReportUtil.logInfo("Clicked on 'Create Account' button.");

            // Assertions
            createAccountPage.verifyAccountCreationUrl();
            extentReportUtil.logPass("Verified the account creation URL.");

            createAccountPage.verifySuccessMessage();
            extentReportUtil.logPass("Verified the success message.");

            createAccountPage.verifyContactInfo(firstName, lastName, email);
            extentReportUtil.logPass("Verified the contact information.");

            // Save the email to a file
            FileUtils.saveToFile("email.txt", email);
            extentReportUtil.logInfo("Saved the email to a file.");
        } catch (Exception e) {
            extentReportUtil.logFail("Test failed: " + e.getMessage());
            extentReportUtil.captureScreenshot(driver, "CreateAccountTestFailure");
            fail(e.getMessage());
        }
    }
}
