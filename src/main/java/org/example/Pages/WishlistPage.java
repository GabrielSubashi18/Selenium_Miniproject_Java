package org.example.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class WishlistPage {
    private WebDriver driver;
    private Logger logger;

    public WishlistPage(WebDriver driver, Logger logger) {
        this.driver = driver;
        this.logger = logger;
    }
    private WebElement refreshAndRetry(By locator) {
        for (int i = 0; i < 3; i++) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.elementToBeClickable(locator));
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                logger.warning("Retrying due to stale element: " + e.getMessage());
            }
        }
        throw new org.openqa.selenium.TimeoutException("Element not found after retries: " + locator);
    }

    public void addToWishlist(int productIndex) {
        try {
            WebElement product = refreshAndRetry(By.cssSelector(".product-item:nth-of-type(" + productIndex + ")"));
            Actions hoverActions = new Actions(driver);
            hoverActions.moveToElement(product).perform();
            logger.info("Hovered over product " + productIndex + ".");

            WebElement wishListButton = refreshAndRetry(By.cssSelector(".product-item:nth-of-type(" + productIndex + ") .action.towishlist"));
            wishListButton.click();
            logger.info("Added product " + productIndex + " to Wish List.");

            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));
        } catch (Exception e) {
            logger.severe("Failed to add product " + productIndex + " to Wish List: " + e.getMessage());
        }
    }
    public void verifyWishlistCount() {
        try {
            // Locate the wishlist count element by its CSS selector
            WebElement toolbarAmount = new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".toolbar-number")));
            String wishListCount = toolbarAmount.getText().trim(); // Extract the text

            logger.info("Wish List count extracted: " + wishListCount);

            // Verify that the count matches the expected value
            assertEquals("2 Item(s)", wishListCount);
            logger.info("Verified Wish List displays 2 items.");
        } catch (Exception e) {
            logger.severe("Failed to verify Wish List count: " + e.getMessage());
        }
    }

}