package com.kelompok1.cucumber.stepdefinitions.web;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.kelompok1.cucumber.pages.web.LaporanSemuaPage;

/**
 * Step Definitions for Laporan Semua page.
 * URL: https://magang.dikahadir.com/laporan/all
 *
 * Background reuses @Given("web user is logged in") from WebLoginSteps.
 * Both classes live in com.kelompok1.cucumber.stepdefinitions.web so
 * WebTestRunner picks them both up automatically.
 */
public class LaporanSemuaSteps {

    private static final Logger logger = LoggerFactory.getLogger(LaporanSemuaSteps.class);

    private LaporanSemuaPage laporanPage() {
        return new LaporanSemuaPage();
    }

    // =========================================================================
    // GIVEN
    // =========================================================================

    @Given("web user is on the laporan semua page")
    public void webUserIsOnLaporanSemuaPage() {
        laporanPage().navigateToPage();
        Assert.assertTrue(laporanPage().isOnPage(),
            "Should be on laporan semua page — https://magang.dikahadir.com/laporan/all");
        logger.info("Navigated to laporan semua page");
    }

    // =========================================================================
    // WHEN — Search
    // =========================================================================

    @When("admin searches laporan by employee name {string}")
    public void adminSearchesLaporanByEmployeeName(String keyword) {
        logger.info("Typing employee name: '{}'", keyword);
        laporanPage().enterSearchKeyword(keyword);
    }

    @When("admin clicks the laporan search button")
    public void adminClicksLaporanSearchButton() {
        laporanPage().clickSearch();
    }

    @When("admin clicks the laporan reset button")
    public void adminClicksLaporanResetButton() {
        laporanPage().clickReset();
    }

    // =========================================================================
    // WHEN — Date Range
    // =========================================================================

    @When("admin selects date range from day {int} to day {int} of June {int}")
    public void adminSelectsDateRange(int startDay, int endDay, int year) {
        logger.info("Selecting date range: day {} to day {} of June {}", startDay, endDay, year);
        // June = month index 5 (0-indexed, matches react-date-range <select> option values)
        laporanPage().selectDateRange(startDay, endDay, 5, year);
    }

    // =========================================================================
    // WHEN — Filter Dialog
    // =========================================================================

    @When("admin opens the laporan filter dialog")
    public void adminOpensLaporanFilterDialog() {
        laporanPage().clickFilterButton();
    }

    @When("admin selects unit {string} in the filter")
    public void adminSelectsUnitInFilter(String unit) {
        logger.info("Selecting unit: '{}'", unit);
        laporanPage().selectUnit(unit);
    }

    @When("admin applies the laporan filter")
    public void adminAppliesLaporanFilter() {
        laporanPage().clickApplyFilter();
    }

    // =========================================================================
    // THEN
    // =========================================================================

    @Then("the laporan table should display results")
    public void laporanTableShouldDisplayResults() {
        laporanPage().waitForTableToHaveResults();
        int actualRows = laporanPage().getTableRowCount();
        Assert.assertTrue(actualRows > 0,
            "Laporan table should have at least one row but found 0");
        logger.info("Table shows {} rows", actualRows);
    }

    @Then("the laporan search input should be empty")
    public void laporanSearchInputShouldBeEmpty() {
        String value = laporanPage().getSearchInputValue();
        Assert.assertTrue(laporanPage().isSearchInputEmpty(),
            "Search input should be empty after reset but contains: '" + value + "'");
        logger.info("Search input is empty after reset — confirmed");
    }
}
