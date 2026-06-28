package com.hadir.steps;

import com.hadir.pages.DashboardPage;
import com.hadir.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;

public class DashboardSteps {
    private WebDriver driver = DriverManager.getDriver();
    private DashboardPage dashboardPage = new DashboardPage(driver);

    @Given("User is on the Hadir Dashboard page")
    public void user_is_on_the_hadir_dashboard_page() {
        dashboardPage.navigateToDashboard();
    }

    @Then("The dashboard metric card {string} should be visible")
    public void the_dashboard_metric_card_should_be_visible(String cardTitle) {
        boolean visible = dashboardPage.isMetricVisible(cardTitle);
        if (!visible) {
            try {
                java.nio.file.Files.writeString(java.nio.file.Paths.get("target/dashboard_dump.html"), driver.getPageSource());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Assert.assertTrue(visible, "Metric card '" + cardTitle + "' is not visible");
    }

    @Then("The value of metric card {string} should load successfully")
    public void the_value_of_metric_card_should_load_successfully(String cardTitle) {
        String value = dashboardPage.getMetricValue(cardTitle);
        Assert.assertNotNull(value, "Value for '" + cardTitle + "' is null");
        Assert.assertFalse(value.isEmpty(), "Value for '" + cardTitle + "' is empty");
        // Verify value is a number
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Assert.fail("Metric value '" + value + "' for '" + cardTitle + "' is not a valid integer");
        }
    }

    @When("User clicks on the profile menu")
    public void user_clicks_on_the_profile_menu() {
        dashboardPage.clickProfileMenu();
    }

    @Then("Option for changing password and logging out should be displayed")
    public void option_for_changing_password_and_logging_out_should_be_displayed() {
        Assert.assertTrue(dashboardPage.isLogoutOptionVisible(), "Logout option not displayed");
        Assert.assertTrue(dashboardPage.isChangePasswordOptionVisible(), "Change Password option not displayed");
    }

    @When("User attempts to access the dashboard directly without logging in")
    public void user_attempts_to_access_the_dashboard_directly_without_logging_in() {
        driver.get("https://magang.dikahadir.com/dashboards/dashboard");
    }

    @Then("User should be redirected back to the login page")
    public void user_should_be_redirected_back_to_the_login_page() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean isRedirected = wait.until(ExpectedConditions.urlContains("authentication/login"));
        Assert.assertTrue(isRedirected, "User was not redirected back to login page. Current URL: " + driver.getCurrentUrl());
    }
}
