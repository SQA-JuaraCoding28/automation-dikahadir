package com.hadir.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By emailField = By.id("email");
    private By passwordField = By.id("password");
    private By masukButton = By.xpath("//button[@type='submit' or text()='Masuk']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToLoginPage() {
        driver.get("https://magang.dikahadir.com/authentication/login");
    }

    public void enterEmail(String email) {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement passInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passInput.clear();
        passInput.sendKeys(password);
    }

    public void clickMasuk() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(masukButton));
        btn.click();
    }

    public void login(String email, String password) {
        navigateToLoginPage();
        enterEmail(email);
        enterPassword(password);
        clickMasuk();
    }
}
