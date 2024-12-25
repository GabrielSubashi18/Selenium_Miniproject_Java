package org.example.Pages;

import org.example.Utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateAccountPage {
    private WebDriver driver;

    @FindBy(css = ".message-success.success.message") // Locator for success message
    private WebElement successMessage;

    @FindBy(css = ".box-content p") // Locator for contact info
    private WebElement contactInfo;

    @FindBy(id = "firstname") // Locator for first name field
    private WebElement firstNameField;

    @FindBy(id = "lastname") // Locator for last name field
    private WebElement lastNameField;

    @FindBy(id = "email_address") // Locator for email field
    private WebElement emailField;

    @FindBy(id = "password") // Locator for password field
    private WebElement passwordField;

    @FindBy(id = "password-confirmation") // Locator for password confirmation field
    private WebElement passwordConfirmationField;

    @FindBy(css = "button[title='Create an Account']") // Locator for create account button
    private WebElement createAccountButton;

    private final String expectedUrl = "https://magento.softwaretestingboard.com/customer/account/";

    public CreateAccountPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillForm(String firstName, String lastName, String email, String password) {
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        passwordConfirmationField.sendKeys(password);
    }

    public void clickCreateAccount() {
        createAccountButton.click();
    }

    public WebElement getFirstNameField() {
        return firstNameField;
    }

    public void verifyAccountCreationUrl() {
        WaitUtils.waitForUrlToBe(driver, expectedUrl, 60);
        assertEquals("URL verification failed!", expectedUrl, driver.getCurrentUrl());
    }

    public void verifySuccessMessage() {
        String actualMessage = successMessage.getText();
        String expectedMessage = "Thank you for registering with Main Website Store.";
        assertTrue("Success message verification failed!", actualMessage.contains(expectedMessage));
    }

    public void verifyContactInfo(String firstName, String lastName, String email) {
        String actualContactInfo = contactInfo.getText();
        assertTrue("Contact info does not contain first and last name!", actualContactInfo.contains(firstName + " " + lastName));
        assertTrue("Contact info does not contain email!", actualContactInfo.contains(email));
    }
}
