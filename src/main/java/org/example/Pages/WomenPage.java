package org.example.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class WomenPage {
    private WebDriver driver;

    @FindBy(id = "ui-id-4") // Women menu
    private WebElement womenMenu;

    @FindBy(id = "ui-id-9") // Tops submenu
    private WebElement topsMenu;

    @FindBy(id = "ui-id-11") // Jackets menu
    private WebElement jacketsMenu;

    public WomenPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Hover over Tops and click Jackets
    public void navigateToJackets() {
        Actions actions = new Actions(driver);
        actions.moveToElement(womenMenu).perform();
        actions.moveToElement(topsMenu).perform();
        jacketsMenu.click();
    }
}
