package com.hadir.steps;

import com.hadir.pages.LoginPage;
import com.hadir.utils.CredentialReader;
import com.hadir.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.Map;

public class LoginSteps {
    private WebDriver driver = DriverManager.getDriver();
    private LoginPage loginPage = new LoginPage(driver);

    @Given("User navigates to Hadir login page")
    public void user_navigates_to_hadir_login_page() {
        loginPage.navigateToLoginPage();
    }

    @When("User logs in with valid admin credentials")
    public void user_logs_in_with_valid_admin_credentials() {
        Map<String, String> creds = CredentialReader.getCredentials();
        loginPage.login(creds.get("adminEmail"), creds.get("adminPassword"));
    }

    @When("User logs in with invalid email {string} and password {string}")
    public void user_logs_in_with_invalid_email_and_password(String email, String password) {
        loginPage.login(email, password);
    }

    @Then("User should be successfully logged in")
    public void user_should_be_successfully_logged_in() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Wait until login URL disappears, meaning we redirected to dashboard
        boolean loginDone = wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("authentication/login")));
        Assert.assertTrue(loginDone, "Login failed: URL still contains authentication/login");
        Assert.assertTrue(driver.getCurrentUrl().contains("magang.dikahadir.com"), "Did not redirect to the Hadir portal");
    }

    @Then("User should remain on the login page and see authentication error")
    public void user_should_remain_on_the_login_page_and_see_authentication_error() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Verify URL still contains login
        Assert.assertTrue(driver.getCurrentUrl().contains("authentication/login"), 
            "User was incorrectly redirected after failed login");
        // Verify alert or field status
        boolean alertPresent = wait.until(ExpectedConditions.or(
            ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='alert']")),
            ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'salah') or contains(text(), 'tidak') or contains(text(), 'Invalid')]"))
        ));
        Assert.assertTrue(alertPresent, "Error message was not displayed for invalid credentials");
    }
}
