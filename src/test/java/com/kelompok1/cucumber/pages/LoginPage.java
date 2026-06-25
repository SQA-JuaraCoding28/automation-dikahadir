package com.kelompok1.cucumber.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.kelompok1.cucumber.core.ConfigReader;
import com.kelompok1.cucumber.core.TestData;

public class LoginPage extends BasePage {

    private final By emailField      = By.id("email");
    private final By passwordField   = By.id("password");
    private final By loginButton     = By.cssSelector("button[type='submit']");
    private final By errorAlert      = By.cssSelector(".MuiAlert-filledError");
    private final By logo            = By.cssSelector("img[alt='bg']");

    public LoginPage() {
        super();
    }

    public LoginPage navigateToLoginPage() {
        String url = ConfigReader.getProperty("base.url", "https://magang.dikahadir.com/absen/login");
        navigateTo(url);
        return this;
    }

    public LoginPage enterEmail(String email) {
        typeText(emailField, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        typeText(passwordField, password);
        return this;
    }

    public LoginPage clickLogin() {
        click(loginButton);
        return this;
    }

    public LoginPage doLogin(String email, String password) {
        logger.info("Performing login with email: {}", email);
        enterEmail(email)
            .enterPassword(password)
            .clickLogin();
        return this;
    }

    public boolean isLoginSuccessful() {
        String url = ConfigReader.getProperty("base.dashboard.url", "https://magang.dikahadir.com/apps/absent");
        return waitForUrlToBe(url);
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(emailField) && isDisplayed(passwordField) && isDisplayed(loginButton);
    }

    public boolean isLogoDisplayed() {
        return isDisplayed(logo);
    }

    public String getErrorMessage() {
        return getText(errorAlert);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorAlert);
    }

    public String getEmailValidationMessage() {
        waitForElementPresent(emailField);
        WebElement emailInput = driver.findElement(emailField);
        String message = emailInput.getAttribute("validationMessage");
        logger.info("Browser validation message: '{}'", message);
        return message;
    }

}
