package com.kelompok1.cucumber.stepdefinitions.mobile;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;

import com.kelompok1.cucumber.pages.mobile.AbsensiPage;
import com.kelompok1.cucumber.pages.mobile.DashboardMobilePage;
import com.kelompok1.cucumber.pages.mobile.LoginMobilePage;

/**
 * Step Definitions for Absensi Keluar (Check-out) feature.
 * Skips if user has not checked in today (no Keluar button).
 *
 * NOTE: @Given("mobile user is logged in and on dashboard") lives in
 * AbsensiMasukSteps to avoid DuplicateStepDefinitionException — Cucumber
 * merges all glue classes in the same package into one runtime.
 */
public class AbsensiKeluarSteps {

    private static final Logger logger = LoggerFactory.getLogger(AbsensiKeluarSteps.class);

    private LoginMobilePage loginPage() {
        return new LoginMobilePage();
    }

    private DashboardMobilePage dashboardPage() {
        return new DashboardMobilePage();
    }

    private AbsensiPage absensiPage() {
        return new AbsensiPage();
    }

    // =========================================================================
    // WHEN — Check & Skip Logic
    // =========================================================================

    @When("user checks if Keluar button is available")
    public void userChecksIfKeluarButtonIsAvailable() {
        logger.info("Checking if Keluar button is available...");
        boolean isAvailable = absensiPage().isKeluarButtonDisplayed();
        logger.info("Keluar button available: {}", isAvailable);
    }

    @Then("skip test if Keluar button is not available")
    public void skipTestIfKeluarButtonIsNotAvailable() {
        if (!absensiPage().isKeluarButtonDisplayed()) {
            logger.warn("Keluar button NOT available — user has not checked in today. Skipping test.");
            throw new SkipException(
                "Skipped: No Keluar button found. User has not checked in today.");
        }
        logger.info("Keluar button is available — continuing with test.");
    }

    // =========================================================================
    // WHEN — Absen Keluar Flow
    // =========================================================================

    @When("user clicks Keluar button on the latest history entry")
    public void userClicksKeluarButton() {
        logger.info("Clicking Keluar button on latest history entry...");
        Assert.assertTrue(absensiPage().isKeluarButtonDisplayed(),
            "Keluar button should be visible on today's entry");
        absensiPage().clickKeluar();
    }

    @When("user fills absen pulang form with note {string}")
    public void userFillsAbsenPulangForm(String note) {
        logger.info("Filling absen pulang form...");

        boolean formPresent = waitForPulangFormToAppear();
        Assert.assertTrue(formPresent,
            "Absen Pulang form should be displayed after clicking Keluar");

        String timeOut = absensiPage().getTimeOutValue();
        Assert.assertFalse(timeOut.isEmpty(),
            "Time Out should be auto-filled with current time");
        logger.info("Time Out auto-filled: {}", timeOut);

        if (!note.isEmpty()) {
            absensiPage().enterKeluarNote(note);
        }
    }

    @When("user submits absen pulang form")
    public void userSubmitsAbsenPulangForm() {
        logger.info("Submitting absen pulang form...");
        absensiPage().submitAbsenPulang();
    }

    // =========================================================================
    // THEN
    // =========================================================================

    @Then("absen pulang should be submitted successfully")
    public void absenPulangSubmittedSuccessfully() {
        logger.info("Verifying absen pulang submission...");
        Assert.assertTrue(dashboardPage().isOnDashboard(),
            "User should return to dashboard after successful absen pulang");
    }

    @Then("Absen Masuk button should not be displayed")
    public void absenMasukButtonShouldNotBeDisplayed() {
        logger.info("Verifying Absen Masuk button is hidden after check-out...");
        Assert.assertFalse(dashboardPage().isAbsenMasukDisplayed(),
            "Absen Masuk button should NOT be displayed after user has checked out");
    }

    // =========================================================================
    // PRIVATE HELPERS
    // =========================================================================

    private boolean waitForPulangFormToAppear() {
        int retries = 5;
        for (int i = 0; i < retries; i++) {
            if (absensiPage().isAbsenPulangFormDisplayed()) {
                logger.info("Pulang form appeared after {} attempt(s)", i + 1);
                return true;
            }
            logger.debug("Pulang form not yet present, retry {}/{}...", i + 1, retries);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }
}
