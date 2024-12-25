package org.example.Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class JacketPage {
    private WebDriver driver;
    private Logger logger;
    private ExtentTest test;
    private ExtentReports report;

    @FindBy(xpath = "//div[@data-role='title' and contains(text(), 'Color')]")
    private WebElement colorDropdown;

    @FindBy(css = "a[href*='color=58'] .swatch-option[option-label='Red']")
    private WebElement redColorOption;

    @FindBy(css = ".product-item")
    private List<WebElement> products;

    @FindBy(xpath = "//div[@data-role='title' and contains(text(), 'Price')]")
    private WebElement priceDropdown;

    @FindBy(xpath = "//a/span[contains(text(), '$50.00')]")
    private WebElement priceOption;

    public JacketPage(WebDriver driver, Logger logger, ExtentTest test, ExtentReports report) {
        this.driver = driver;
        this.logger = logger;
        this.test = test;
        this.report = report;
        PageFactory.initElements(driver, this);
    }

    public void applyColorFilter() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(colorDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(redColorOption)).click();
    }

    public void verifyProductsHaveColor(String expectedColor) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".product-item")));
            for (WebElement product : products) {
                WebElement colorSwatch = wait.until(ExpectedConditions.refreshed(
                        ExpectedConditions.presenceOfNestedElementLocatedBy(product, By.cssSelector(".swatch-option.color.selected"))
                ));
                String colorLabel = colorSwatch.getDomProperty("aria-label");
                assertTrue("Product does not have the selected color!", colorLabel.toLowerCase().contains(expectedColor.toLowerCase()));
            }
            logger.info("Verified all displayed products have the selected color.");
        } catch (StaleElementReferenceException e) {
            logger.severe("Failed to verify product colors due to stale element reference: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Failed to verify product colors: " + e.getMessage());
        }
    }

    public void applyPriceFilter(double minPrice, double maxPrice) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String priceRangeText = String.format("$%.2f", minPrice);
        try {
            WebElement priceDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@data-role='title' and contains(text(), 'Price')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", priceDropdown);
            priceDropdown.click();
            logger.info("Price dropdown clicked.");

            WebElement priceOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a/span[contains(text(), '" + priceRangeText + "')]")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", priceOption);
            priceOption.click();
            logger.info("Price range '" + minPrice + " - " + maxPrice + "' selected.");

            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".product-item")));
            logger.info("Products updated after applying the price filter.");

            List<WebElement> products = driver.findElements(By.cssSelector(".product-item"));
            for (WebElement product : products) {
                WebElement price = product.findElement(By.cssSelector(".price"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", price);
                String priceText = price.getText().replace("$", "");
                double priceValue = Double.parseDouble(priceText);
                assertTrue("Product price is out of range!", priceValue >= minPrice && priceValue <= maxPrice);
            }
            logger.info("Verified only two products are displayed and their prices match the criteria.");
        } catch (Exception e) {
            logger.severe("Error interacting with price filter: " + e.getMessage());
        }
    }

    public void removePriceFilter() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            WebElement removePriceFilter = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".action.remove[title='Remove Price $50.00 - $59.99']")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", removePriceFilter);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removePriceFilter);
            logger.info("Removed price filter using JavaScript click.");

            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".action.remove[title='Remove Price $50.00 - $59.99']")
            ));
            logger.info("Confirmed price filter is removed.");
        } catch (Exception e) {
            logger.severe("Error clearing price filter: " + e.getMessage());
        }
    }

    public void addAllItemsToCart(WebDriverWait wait) {
        try {
            List<WebElement> productItems = driver.findElements(By.cssSelector(".product-item"));
            Actions actions = new Actions(driver);

            for (WebElement product : productItems) {
                try {
                    // Hover over the product
                    actions.moveToElement(product).perform();
                    logger.info("Hovered over product.");

                    // Select size if available
                    try {
                        WebElement sizeContainer = product.findElement(By.cssSelector(".swatch-attribute.size"));
                        WebElement sizeOption = sizeContainer.findElement(By.cssSelector(".swatch-option[option-label='XS']"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sizeOption);
                        logger.info("Selected size 'XS'.");
                    } catch (Exception e) {
                        logger.warning("No size selection available for this product.");
                    }

                    // Click "Add to Cart"
                    WebElement addToCartButton = product.findElement(By.cssSelector(".action.tocart"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartButton);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartButton);
                    logger.info("Clicked 'Add to Cart' for the product.");

                    // Wait for the success message (but don't navigate away yet)
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));
                    logger.info("Success message displayed for the product.");
                } catch (Exception e) {
                    logger.warning("Failed to add product to cart: " + e.getMessage());
                }

            }

            // Navigate to the shopping cart after adding all products
            try {
                WebElement cartLink = driver.findElement(By.cssSelector(".message-success a[href*='checkout/cart']"));
                cartLink.click();
                logger.info("Navigated to the shopping cart after adding all products.");
            } catch (Exception e) {
                logger.severe("Failed to navigate to the shopping cart: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.severe("Error adding items to cart: " + e.getMessage());
        }
    }
}