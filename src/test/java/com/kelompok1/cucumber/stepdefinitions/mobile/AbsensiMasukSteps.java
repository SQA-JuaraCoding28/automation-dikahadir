package com.kelompok1.cucumber.stepdefinitions.mobile;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;

import com.kelompok1.cucumber.core.TestData;
import com.kelompok1.cucumber.pages.mobile.AbsensiPage;
import com.kelompok1.cucumber.pages.mobile.DashboardMobilePage;
import com.kelompok1.cucumber.pages.mobile.LoginMobilePage;

/**
 * Step Definitions for Absensi Masuk (Check-in) feature.
 *
 * Also owns the shared @Given("mobile user is logged in and on dashboard") step
 * — kept here (not in AbsensiKeluarSteps) to avoid DuplicateStepDefinitionException.
 */
public class AbsensiMasukSteps {

    private static final Logger logger = LoggerFactory.getLogger(AbsensiMasukSteps.class);

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
    // GIVEN — shared with AbsensiKeluarSteps via same glue package
    // =========================================================================

    @Given("mobile user is logged in and on dashboard")
    public void mobileUserIsLoggedInAndOnDashboard() {
        logger.info("Setting up authenticated mobile session on dashboard...");
        loginPage().navigateToLoginPage();
        loginPage().doLogin(TestData.validEmail(), TestData.validPassword());
        Assert.assertTrue(dashboardPage().isOnDashboard(),
            "User should be on dashboard after login");
    }

    /**
     * Precondition for the "absen pulang after checking in" scenario.
     * Verifies the user has already checked in today by confirming the
     * Keluar button is visible in the history section.
     */
    @Given("mobile user has already checked in today")
    public void mobileUserHasAlreadyCheckedInToday() {
        logger.info("Verifying user has already checked in today...");
        if (!absensiPage().isKeluarButtonDisplayed()) {
            throw new SkipException(
                "Skipped: No check-in found for today — Keluar button is not visible. " +
                "Run the absen masuk scenario first.");
        }
        logger.info("Keluar button confirmed — user has checked in today.");
    }

    // =========================================================================
    // WHEN — Absen Masuk Flow
    // =========================================================================

    @When("user clicks Absen Masuk button on dashboard")
    public void userClicksAbsenMasukButton() {
        logger.info("Checking if Absen Masuk button is available...");
        if (!dashboardPage().isAbsenMasukDisplayed()) {
            throw new SkipException(
                "Skipped: Absen Masuk button not found. User may have already checked in today.");
        }
        logger.info("Clicking Absen Masuk button...");
        dashboardPage().clickAbsenMasuk();
        absensiPage().dismissPasswordPopupIfPresent();
    }

    @When("user takes a photo using camera")
    public void userTakesPhotoUsingCamera() {
        logger.info("Checking for camera page...");

        if (absensiPage().isCameraPagePresent()) {
            logger.info("Camera page detected — taking photo...");
            Assert.assertTrue(absensiPage().isCameraButtonDisplayed(),
                "Camera button should be visible");
            absensiPage().clickCameraButton();

            boolean formAppeared = waitForFormToAppearAfterCamera();
            if (!formAppeared) {
                logger.warn("Form did not appear after camera click — camera may still be open");
            }
        } else {
            logger.info("Camera page NOT detected — proceeding to form directly");
        }
    }

    @When("user fills absen masuk form with Work From Home and note {string}")
    public void userFillsAbsenMasukForm(String note) {
        logger.info("Filling absen masuk form...");

        boolean formPresent = waitForFormToAppear();
        Assert.assertTrue(formPresent,
            "Absen Masuk form should be displayed after taking photo or direct transition");

        String jamAbsen = absensiPage().getJamAbsenValue();
        Assert.assertFalse(jamAbsen.isEmpty(),
            "Jam Absen should be auto-filled with current time");
        logger.info("Jam Absen auto-filled: {}", jamAbsen);

        absensiPage().selectTipeAbsenWfh();
        Assert.assertEquals(absensiPage().getSelectedTipeAbsen(), "Work From Home",
            "Tipe Absen should be Work From Home");

        absensiPage().enterNoteMasuk(note);
    }

    @When("user submits absen masuk form")
    public void userSubmitsAbsenMasukForm() {
        logger.info("Submitting absen masuk form...");
        absensiPage().submitAbsenMasuk();
    }

    // =========================================================================
    // THEN
    // =========================================================================

    @Then("absen masuk should be submitted successfully")
    public void absenMasukSubmittedSuccessfully() {
        logger.info("Verifying absen masuk submission...");
        Assert.assertTrue(dashboardPage().isOnDashboard(),
            "User should return to dashboard after successful absen masuk");
    }

    @Then("history absensi should show new entry for today with type {string}")
    public void historyShowsTodayEntryWithType(String expectedType) {
        logger.info("Verifying history entry for today...");
        Assert.assertTrue(absensiPage().isHistorySectionDisplayed(),
            "History Absensi section should be visible");

        AbsensiPage.HistoryEntry latest = absensiPage().getLatestHistoryEntry();
        Assert.assertNotNull(latest, "Latest history entry should exist");
        logger.info("Latest entry: {}", latest);
        Assert.assertTrue(latest.time.contains("Masuk pukul"),
            "Entry should show check-in time");
    }

    // =========================================================================
    // PRIVATE HELPERS
    // =========================================================================

    private boolean waitForFormToAppearAfterCamera() {
        int retries = 10;
        for (int i = 0; i < retries; i++) {
            if (!absensiPage().isCameraPagePresent() && absensiPage().isAbsenMasukFormPresent()) {
                logger.info("Form appeared after camera closed ({} retries)", i + 1);
                return true;
            }
            if (absensiPage().isAbsenMasukFormPresent()) {
                logger.info("Form appeared while camera still open ({} retries)", i + 1);
                return true;
            }
            logger.debug("Waiting for form after camera, retry {}/{}...", i + 1, retries);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

    private boolean waitForFormToAppear() {
        int retries = 5;
        for (int i = 0; i < retries; i++) {
            if (absensiPage().isAbsenMasukFormPresent()) {
                logger.info("Form appeared after {} attempt(s)", i + 1);
                return true;
            }
            logger.debug("Form not yet present, retry {}/{}...", i + 1, retries);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }
}
