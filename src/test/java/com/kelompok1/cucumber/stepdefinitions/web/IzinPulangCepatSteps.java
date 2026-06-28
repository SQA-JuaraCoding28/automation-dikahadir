package com.kelompok1.cucumber.stepdefinitions.web;

import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.web.IzinPulangCepatPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import java.util.List;

public class IzinPulangCepatSteps {
    private IzinPulangCepatPage izinPage = new IzinPulangCepatPage();
    private String initialPaginationText = "";

    @Given("User is logged in and on the Izin Pulang Cepat page")
    public void user_is_logged_in_and_on_the_izin_pulang_cepat_page() {
        izinPage.navigateToPage();
    }

    @When("User enters search keyword {string}")
    public void user_enters_search_keyword(String keyword) {
        izinPage.enterSearchKeyword(keyword);
    }

    @When("User clicks search button")
    public void user_clicks_search_button() {
        izinPage.clickSearch();
    }

    @Then("The table should show records matching {string}")
    public void the_table_should_show_records_matching(String keyword) {
        if (!izinPage.isNoDataDisplayed()) {
            String userName = izinPage.getFirstRowUserName();
            if (userName != null) {
                Assert.assertTrue(userName.toLowerCase().contains(keyword.toLowerCase()), 
                    "Expected first row user name '" + userName + "' to contain keyword '" + keyword + "'");
            }
        } else {
            System.out.println("No data displayed for keyword: " + keyword);
        }
    }

    @When("User clicks reset button")
    public void user_clicks_reset_button() {
        izinPage.clickReset();
    }

    @Then("The search field should be empty")
    public void the_search_field_should_be_empty() {
        WebElement input = DriverManager.getDriver().findElement(By.id("search"));
        String val = input.getAttribute("value");
        Assert.assertTrue(val == null || val.isEmpty(), "Search field is not empty after reset: " + val);
    }

    @When("User enters start date {string} and end date {string}")
    public void user_enters_start_date_and_end_date(String startDate, String endDate) {
        izinPage.enterStartDate(startDate);
        izinPage.enterEndDate(endDate);
    }

    @Then("The table should show records filtered by dates")
    public void the_table_should_show_records_filtered_by_dates() {
        List<WebElement> rows = izinPage.getTableRows();
        Assert.assertNotNull(rows, "Table rows should not be null");
    }

    @Given("User stores initial pagination state")
    public void user_stores_initial_pagination_state() {
        initialPaginationText = izinPage.getPaginationText();
    }

    @When("User clicks next page button")
    public void user_clicks_next_page_button() {
        izinPage.clickNextPage();
    }

    @Then("The table should show the next page of records")
    public void the_table_should_show_the_next_page_of_records() {
        String newPaginationText = izinPage.getPaginationText();
        Assert.assertNotEquals(newPaginationText, initialPaginationText, 
            "Pagination text did not change after navigating to next page");
    }

    @When("User clicks previous page button")
    public void user_clicks_previous_page_button() {
        izinPage.clickPrevPage();
    }

    @Then("The table should show the previous page of records")
    public void the_table_should_show_the_previous_page_of_records() {
        String newPaginationText = izinPage.getPaginationText();
        Assert.assertNotNull(newPaginationText, "Pagination text should be present");
    }

    @When("User changes rows per page to {string}")
    public void user_changes_rows_per_page_to(String size) {
        izinPage.selectRowsPerPage(size);
    }

    @Then("The table should show at most {int} records")
    public void the_table_should_show_at_most_records(Integer limit) {
        List<WebElement> rows = izinPage.getTableRows();
        int rowCount = rows.size();
        
        if (izinPage.isNoDataDisplayed()) {
            rowCount = 0;
        }
        
        Assert.assertTrue(rowCount <= limit, 
            "Row count (" + rowCount + ") exceeds rows per page limit (" + limit + ")");
    }
}
