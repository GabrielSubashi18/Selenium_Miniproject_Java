package org.example.Pages;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class ShoppingCartPage {
    private WebDriver driver;
    private Logger logger;
    private ExtentTest test;
    private ExtentReports report;

    @FindBy(css = ".action.showcart")
    private WebElement cartButton;

    @FindBy(css = ".action.delete")
    private List<WebElement> removeItemButtons;

    @FindBy(css = ".cart-empty")
    private WebElement emptyCartMessage;

    public ShoppingCartPage(WebDriver driver, Logger logger, ExtentTest test, ExtentReports report) {
        this.driver = driver;
        this.logger = logger;
        this.test = test;
        this.report = report;
        PageFactory.initElements(driver, this);
    }


    public void verifyOrderTotal(WebDriverWait wait) {
        try {
            // Fetch subtotal from the cart summary
            WebElement subtotalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".cart-totals .totals.sub .price")));
            String subtotalText = subtotalElement.getText().replace("$", "").trim();
            double displayedSubtotal = Double.parseDouble(subtotalText);
            logger.info("Fetched displayed subtotal: $" + displayedSubtotal);

            // Calculate subtotal by iterating through cart items
            List<WebElement> cartRows = driver.findElements(By.cssSelector("#shopping-cart-table tbody.cart.item"));
            double calculatedSubtotal = 0.0;

            for (WebElement row : cartRows) {
                // Extract price and quantity for each product
                WebElement priceElement = row.findElement(By.cssSelector(".col.price .price"));
                WebElement quantityElement = row.findElement(By.cssSelector(".col.qty input"));

                String priceText = priceElement.getText().replace("$", "").trim();
                String quantityText = quantityElement.getDomProperty("value").trim();

                double price = Double.parseDouble(priceText);
                int quantity = Integer.parseInt(quantityText);

                // Accumulate subtotal
                calculatedSubtotal += price * quantity;
            }

            logger.info("Calculated subtotal from cart items: $" + calculatedSubtotal);

            // Assert that calculated subtotal matches displayed subtotal
            assertEquals("Subtotal mismatch between calculated and displayed values",
                    calculatedSubtotal, displayedSubtotal, 0.01);
            logger.info("Verified that calculated subtotal matches displayed subtotal.");
        } catch (Exception e) {
            logger.severe("Error verifying subtotal: " + e.getMessage());
        }
    }
    public void verifyAndEmptyCart(WebDriverWait wait) {
        try {
            while (true) {
                List<WebElement> cartItems = driver.findElements(By.cssSelector("#shopping-cart-table tbody tr"));
                if (cartItems.isEmpty()) {
                    logger.info("Shopping cart is already empty.");
                    break;
                }

                WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                        driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody/tr[2]/td/div/a[3]"))));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", deleteButton);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteButton);
                logger.info("Clicked delete for the second item in the cart.");

                wait.until(ExpectedConditions.stalenessOf(cartItems.get(0)));
            }

            WebElement emptyCartMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".cart-empty .message")));
            assert "You have no items in your shopping cart.".equals(emptyCartMessage.getText().trim());
            logger.info("Verified that the shopping cart is empty.");
        } catch (Exception e) {
            logger.severe("Error during cart verification: " + e.getMessage());
            throw e;
        }
    }
}