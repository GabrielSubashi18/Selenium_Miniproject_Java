package Miniproject;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.example.Pages.*;
import org.example.Utils.FileUtils;
import org.example.Utils.ScreenshotTestWatcher;
import org.example.Utils.WebDriverManagerUtil;
import org.example.Utils.ExtentReportUtil;
import org.junit.Test;
import org.junit.Rule;

import java.util.logging.Logger;

public class WishListTest extends WebDriverManagerUtil {
    private static final Logger logger = Logger.getLogger(WishListTest.class.getName());
    private ExtentReportUtil extentReportUtil;
    private ExtentReports report;
    private ExtentTest test;

    @Rule
    public ScreenshotTestWatcher screenshotTestWatcher = new ScreenshotTestWatcher(driver);

    @Test
    public void testWishList() {
        String email = FileUtils.readFromFile("email.txt");
        String password = "Password123!";

        // Initialize ExtentReportUtil
        extentReportUtil = new ExtentReportUtil("WishListTest Report");
        extentReportUtil.createTest("testWishList");

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
        JacketPage jacketPage = new JacketPage(driver, logger, extentReportUtil.getTest(), extentReportUtil.getReport());

        // Apply and verify color filter
        jacketPage.applyColorFilter();
        extentReportUtil.logInfo("Applied 'Red' color filter.");
        jacketPage.verifyProductsHaveColor("red");

        // Apply and verify price filter
        jacketPage.applyPriceFilter(50.00, 59.99);
        extentReportUtil.logInfo("Applied price filter '$50.00 - $59.99'.");

        // Remove price filter
        jacketPage.removePriceFilter();

        // Add products to Wish List
        WishlistPage wishlistPage = new WishlistPage(driver, logger);
        wishlistPage.addToWishlist(1);
        driver.navigate().back();
        wishlistPage.addToWishlist(2);

        // Verify Wish List count
        wishlistPage.verifyWishlistCount();

        // Close the driver
        driver.quit();
    }
}