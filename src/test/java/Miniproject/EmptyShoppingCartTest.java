package Miniproject;
import org.junit.Rule;
import org.example.Pages.*;
import org.example.Utils.FileUtils;
import org.example.Utils.ScreenshotTestWatcher;
import org.example.Utils.WebDriverManagerUtil;
import org.junit.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.example.Utils.ExtentReportUtil;

import java.time.Duration;
import java.util.logging.Logger;

public class EmptyShoppingCartTest extends WebDriverManagerUtil {
    private static final Logger logger = Logger.getLogger(EmptyShoppingCartTest.class.getName());
    private ExtentReportUtil extentReportUtil;
    private ExtentReports report;
    private ExtentTest test;
    private String email;
    private String password = "Password123!"; // Use the password from CreateAccountTest

    @Rule
    public ScreenshotTestWatcher screenshotTestWatcher = new ScreenshotTestWatcher(driver);

    @Test
    public void testEmptyShoppingCart() {
        // Initialize ExtentReportUtil
        extentReportUtil = new ExtentReportUtil("EmptyShoppingCartTest Report");
        extentReportUtil.createTest("testEmptyShoppingCart");
        test = extentReportUtil.getTest();
        report = extentReportUtil.getReport();
        driver.get("https://magento.softwaretestingboard.com/");
        extentReportUtil.logInfo("Navigated to the home page.");

        // Login process
        HomePage homePage = new HomePage(driver);
        homePage.clickSignIn();
        extentReportUtil.logInfo("Clicked on 'Sign In'.");

        SignInPage signInPage = new SignInPage(driver);
        String email = FileUtils.readFromFile("email.txt");
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

        // Apply and verify price filter
        jacketPage.applyPriceFilter(50.00, 59.99);
        extentReportUtil.logInfo("Applied price filter '$50.00 - $59.99'.");

        // Add products to cart
        jacketPage.addAllItemsToCart(new WebDriverWait(driver, Duration.ofSeconds(10)));
        extentReportUtil.logInfo("Added all items to cart.");

        // Verify cart total
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver, logger, test, report);
        shoppingCartPage.verifyOrderTotal(new WebDriverWait(driver, Duration.ofSeconds(10)));

        // Empty the cart
        shoppingCartPage.verifyAndEmptyCart(new WebDriverWait(driver, Duration.ofSeconds(10)));

        driver.quit();
        }

}
