package com.kelompok1.cucumber.stepdefinitions.web;

import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.web.DashboardPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class DashboardSteps {
    private DashboardPage dashboardPage = new DashboardPage();

    @Given("User is on the Hadir Dashboard page")
    public void user_is_on_the_hadir_dashboard_page() {
        dashboardPage.navigateToDashboard();
    }

    @Then("The dashboard metric card {string} should be visible")
    public void the_dashboard_metric_card_should_be_visible(String cardTitle) {
        Assert.assertTrue(dashboardPage.isMetricVisible(cardTitle), 
            "Metric card '" + cardTitle + "' is not visible");
    }

    @Then("The value of metric card {string} should load successfully")
    public void the_value_of_metric_card_should_load_successfully(String cardTitle) {
        String valText = dashboardPage.getMetricValue(cardTitle);
        Assert.assertFalse(valText.isEmpty(), "Metric card '" + cardTitle + "' value is empty");
        
        try {
            int count = Integer.parseInt(valText);
            Assert.assertTrue(count >= 0, "Metric count should be a non-negative integer, got: " + count);
        } catch (NumberFormatException e) {
            Assert.fail("Metric count for '" + cardTitle + "' is not a valid integer: '" + valText + "'");
        }
    }

    @When("User clicks profile menu dropdown")
    public void user_clicks_profile_menu_dropdown() {
        dashboardPage.clickProfileMenu();
    }

    @Then("User should see logout and change password options")
    public void user_should_see_logout_and_change_password_options() {
        Assert.assertTrue(dashboardPage.isLogoutOptionVisible(), "Logout option is not visible");
        Assert.assertTrue(dashboardPage.isChangePasswordOptionVisible(), "Change Password option is not visible");
    }

    @When("User clicks logout button")
    public void user_clicks_logout_button() {
        dashboardPage.clickLogout();
    }

    @Then("User should be redirected to login page")
    public void user_should_be_redirected_to_login_page() {
        boolean redirected = dashboardPage.waitForUrlToContain("login");
        Assert.assertTrue(redirected, "User was not redirected to the login page. Current URL: " + DriverManager.getDriver().getCurrentUrl());
    }

    @Given("User attempts to navigate to dashboard page directly without logging in")
    public void user_attempts_to_navigate_to_dashboard_page_directly_without_logging_in() {
        // Clear all cookies/storage to ensure not logged in
        DriverManager.getDriver().manage().deleteAllCookies();
        dashboardPage.navigateToDashboard();
    }
}
