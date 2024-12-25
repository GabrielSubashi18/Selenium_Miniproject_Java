package Miniproject;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.example.Pages.*;
import org.example.Utils.ExtentReportUtil;
import org.example.Utils.FileUtils;
import org.example.Utils.ScreenshotTestWatcher;
import org.junit.Test;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class ShoppingCartTest {
    private static final Logger logger = Logger.getLogger(ShoppingCartTest.class.getName());
    private WebDriver driver;
    private ExtentReportUtil extentReportUtil;
    private ExtentReports report;
    private ExtentTest test;

    @Rule
    public ScreenshotTestWatcher screenshotTestWatcher = new ScreenshotTestWatcher(driver);

    @Test
    public void testShoppingCart() {
        driver = new ChromeDriver();
        String email = FileUtils.readFromFile("email.txt");
        String password = "Password123!";

        // Initialize ExtentReportUtil
        extentReportUtil = new ExtentReportUtil("ShoppingCartTest Report");
        extentReportUtil.createTest("testShoppingCart");
        test = extentReportUtil.getTest();
        report = extentReportUtil.getReport();

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

        // Apply and verify price filter
        jacketPage.applyPriceFilter(50.00, 59.99);
        extentReportUtil.logInfo("Applied price filter '$50.00 - $59.99'.");

        // Add products to cart
        jacketPage.addAllItemsToCart(new WebDriverWait(driver, Duration.ofSeconds(10)));
        extentReportUtil.logInfo("Added all items to cart.");

        // Verify cart total
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver, logger, test, report);
        shoppingCartPage.verifyOrderTotal(new WebDriverWait(driver, Duration.ofSeconds(10)));

        driver.quit();
    }
}