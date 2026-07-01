package com.kelompok1.cucumber.stepdefinitions.web;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.kelompok1.cucumber.pages.web.LaporanDownloadAbsenPage;

/**
 * Step Definitions for Download Absen report page.
 * URL: https://magang.dikahadir.com/laporan/generate-report-type
 */
public class LaporanDownloadAbsenSteps {

    private static final Logger logger = LoggerFactory.getLogger(LaporanDownloadAbsenSteps.class);

    private LaporanDownloadAbsenPage downloadAbsenPage() {
        return new LaporanDownloadAbsenPage();
    }

    // =========================================================================
    // GIVEN
    // =========================================================================

    @Given("web user navigates to Download Absen page via sidebar")
    public void webUserNavigatesToDownloadAbsenPageViaSidebar() {
        downloadAbsenPage().navigateViaSidebar();
        Assert.assertTrue(downloadAbsenPage().isOnPage(),
            "Should be on Download Absen page — https://magang.dikahadir.com/laporan/generate-report-type");
        logger.info("Navigated to Download Absen page via sidebar");
    }

    // =========================================================================
    // WHEN — Date Range
    // =========================================================================

    @When("admin selects absen date range from day {int} month {int} year {int} to day {int} month {int} year {int}")
    public void adminSelectsAbsenDateRange(int startDay, int startMonth, int startYear,
                                           int endDay, int endMonth, int endYear) {
        logger.info("Selecting absen date range: {}/{}/{} to {}/{}/{}",
            startDay, startMonth, startYear, endDay, endMonth, endYear);
        downloadAbsenPage().selectDateRange(startDay, startMonth, startYear, endDay, endMonth, endYear);
    }

    // =========================================================================
    // WHEN — Filters
    // =========================================================================

    @When("admin selects NIK {string}")
    public void adminSelectsNik(String nik) {
        logger.info("Selecting NIK: '{}'", nik);
        downloadAbsenPage().selectNik(nik);
    }

    @When("admin selects nama user {string}")
    public void adminSelectsNamaUser(String nama) {
        logger.info("Selecting nama user: '{}'", nama);
        downloadAbsenPage().selectNamaUser(nama);
    }

    @When("admin selects nama upliner {string}")
    public void adminSelectsNamaUpliner(String nama) {
        logger.info("Selecting nama upliner: '{}'", nama);
        downloadAbsenPage().selectNamaUpliner(nama);
    }

    @When("admin selects divisi {string}")
    public void adminSelectsDivisi(String divisi) {
        logger.info("Selecting divisi: '{}'", divisi);
        downloadAbsenPage().selectDivisi(divisi);
    }

    @When("admin selects unit {string}")
    public void adminSelectsUnit(String unit) {
        logger.info("Selecting unit: '{}'", unit);
        downloadAbsenPage().selectUnit(unit);
    }

    @When("admin selects tipe report {string}")
    public void adminSelectsTipeReport(String tipe) {
        logger.info("Selecting tipe report: '{}'", tipe);
        downloadAbsenPage().selectTipeReport(tipe);
    }

    // =========================================================================
    // WHEN — Buttons
    // =========================================================================

    @When("admin clicks the absen download button")
    public void adminClicksAbsenDownloadButton() {
        downloadAbsenPage().clickDownload();
    }

    @When("admin clicks the absen reset button")
    public void adminClicksAbsenResetButton() {
        downloadAbsenPage().clickReset();
    }

    // =========================================================================
    // THEN
    // =========================================================================

    @Then("download should complete without error")
    public void downloadShouldCompleteWithoutError() {
        Assert.assertFalse(downloadAbsenPage().isErrorDisplayed(),
            "Expected download to succeed without error, but an error alert was found");
        logger.info("Download completed without error");
    }

    @Then("the absen filters should be cleared")
    public void absenFiltersShouldBeCleared() {
        Assert.assertTrue(downloadAbsenPage().areFiltersCleared(),
            "Expected filters to be cleared after reset, but some values remain");
        logger.info("All filters cleared after reset — confirmed");
    }
}
