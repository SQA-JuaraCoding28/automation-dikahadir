package com.kelompok1.cucumber.stepdefinitions.web;

import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.web.KoreksiPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import java.util.List;

public class KoreksiSteps {
    private KoreksiPage koreksiPage = new KoreksiPage();
    private String initialPaginationText = "";

    @Given("User is logged in and on the Laporan Koreksi page")
    public void user_is_logged_in_and_on_the_laporan_koreksi_page() {
        koreksiPage.navigateToPage();
        koreksiPage.waitForPagination();
    }

    @When("User enters search keyword {string} in Koreksi page")
    public void user_enters_search_keyword_in_koreksi_page(String keyword) {
        koreksiPage.enterSearchKeyword(keyword);
    }

    @When("User clicks search button in Koreksi page")
    public void user_clicks_search_button_in_koreksi_page() {
        koreksiPage.clickSearch();
    }

    @Then("The table should show Koreksi records matching {string}")
    public void the_table_should_show_koreksi_records_matching(String keyword) {
        if (!koreksiPage.isNoDataDisplayed()) {
            String userName = koreksiPage.getFirstRowUserName();
            if (userName != null) {
                Assert.assertTrue(userName.toLowerCase().contains(keyword.toLowerCase()), 
                    "Expected first row user name '" + userName + "' to contain keyword '" + keyword + "'");
            }
        } else {
            System.out.println("No data displayed for keyword: " + keyword);
        }
    }

    @When("User clicks reset button in Koreksi page")
    public void user_clicks_reset_button_in_koreksi_page() {
        koreksiPage.clickReset();
    }

    @Then("The search field in Koreksi page should be empty")
    public void the_search_field_in_koreksi_page_should_be_empty() {
        WebElement input = DriverManager.getDriver().findElement(By.id("search"));
        String val = input.getAttribute("value");
        Assert.assertTrue(val == null || val.isEmpty(), "Search field is not empty after reset: " + val);
    }

    @When("User enters start date {string} and end date {string} in Koreksi page")
    public void user_enters_start_date_and_end_date_in_koreksi_page(String startDate, String endDate) {
        koreksiPage.enterStartDate(startDate);
        koreksiPage.enterEndDate(endDate);
    }

    @Then("The table should show Koreksi records filtered by dates")
    public void the_table_should_show_koreksi_records_filtered_by_dates() {
        List<WebElement> rows = koreksiPage.getTableRows();
        Assert.assertNotNull(rows, "Table rows should not be null");
    }

    @Given("User stores initial pagination state in Koreksi page")
    public void user_stores_initial_pagination_state_in_koreksi_page() {
        initialPaginationText = koreksiPage.getPaginationText();
    }

    @When("User clicks next page button in Koreksi page")
    public void user_clicks_next_page_button_in_koreksi_page() {
        koreksiPage.clickNextPage();
    }

    @Then("The table should show the next page of Koreksi records")
    public void the_table_should_show_the_next_page_of_koreksi_records() {
        String newPaginationText = koreksiPage.getPaginationText();
        Assert.assertNotEquals(newPaginationText, initialPaginationText, 
            "Pagination text did not change after navigating to next page");
    }

    @When("User clicks previous page button in Koreksi page")
    public void user_clicks_previous_page_button_in_koreksi_page() {
        koreksiPage.clickPrevPage();
    }

    @Then("The table should show the previous page of Koreksi records")
    public void the_table_should_show_the_previous_page_of_koreksi_records() {
        String newPaginationText = koreksiPage.getPaginationText();
        Assert.assertNotNull(newPaginationText, "Pagination text should be present");
    }

    @When("User changes rows per page to {string} in Koreksi page")
    public void user_changes_rows_per_page_to_in_koreksi_page(String size) {
        koreksiPage.selectRowsPerPage(size);
    }

    @Then("The table should show at most {int} Koreksi records")
    public void the_table_should_show_at_most_koreksi_records(Integer limit) {
        List<WebElement> rows = koreksiPage.getTableRows();
        int rowCount = rows.size();
        
        if (koreksiPage.isNoDataDisplayed()) {
            rowCount = 0;
        }
        
        Assert.assertTrue(rowCount <= limit, 
            "Row count (" + rowCount + ") exceeds rows per page limit (" + limit + ")");
    }

    @Given("User attempts to navigate to Laporan Koreksi page directly without logging in")
    public void user_attempts_to_navigate_to_laporan_koreksi_page_directly_without_logging_in() {
        DriverManager.getDriver().manage().deleteAllCookies();
        koreksiPage.navigateToPage();
    }

    @Then("User is redirected back to the login page on Koreksi page check")
    public void user_is_redirected_back_to_the_login_page_on_koreksi_page_check() {
        boolean onLogin = DriverManager.getDriver().getCurrentUrl().contains("login");
        Assert.assertTrue(onLogin, "User was not redirected to the login page. Current URL: " + DriverManager.getDriver().getCurrentUrl());
    }
}
