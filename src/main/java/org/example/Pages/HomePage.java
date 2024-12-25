package org.example.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private WebDriver driver;

    // Locator for User Profile or Account button
    @FindBy(css = "a.account > span.label") // Update the CSS selector to match your application
    private WebElement userProfileLink;
    // Locator for Create Account button
    @FindBy(linkText = "Create an Account")
    private WebElement createAccountLink;

    @FindBy(css = "button.action.switch[data-action='customer-menu-toggle']") // Locator for user profile dropdown
    private WebElement userProfileDropdown;

    @FindBy(linkText = "Sign Out")
    private WebElement signOutLink;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void clickSignIn() {
        driver.findElement(By.linkText("Sign In")).click();
    }
    // Method to click on Create Account
    public void clickCreateAccount() {
        createAccountLink.click();
    }
    public void clickUserProfileDropdown() {
        userProfileDropdown.click();
    }

    public void signOut() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(signOutLink)).click();
    }
}
