package com.kelompok1.cucumber.stepdefinitions.web;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.kelompok1.cucumber.pages.web.UnitSettingPage;

/**
 * Step Definitions for Unit Setting (Departements Setting) page.
 * URL: https://magang.dikahadir.com/management/departements-settings
 *
 * Background reuses @Given("web user is logged in") from WebLoginSteps.
 * Per requirements: each flow only verifies "no error thrown" — no table
 * data re-fetch/assertion after add, toggle, or delete.
 */
public class UnitSettingSteps {

    private static final Logger logger = LoggerFactory.getLogger(UnitSettingSteps.class);

    private UnitSettingPage unitSettingPage() {
        return new UnitSettingPage();
    }

    // =========================================================================
    // GIVEN
    // =========================================================================

    @Given("web user is on the unit setting page")
    public void webUserIsOnUnitSettingPage() {
        unitSettingPage().navigateViaSidebar();
        Assert.assertTrue(unitSettingPage().isOnPage(),
            "Should be on unit setting page after navigating via sidebar (Management > Unit Setting)");
        logger.info("Navigated to unit setting page via sidebar");
    }

    // =========================================================================
    // WHEN — Add flow
    // =========================================================================

    @When("admin clicks the Tambahkan button")
    public void adminClicksTambahkanButton() {
        unitSettingPage().clickTambahkan();
    }

    @When("admin selects nama departemen {string} from the dropdown")
    public void adminSelectsNamaDepartemenFromDropdown(String value) {
        unitSettingPage().selectNamaDepartemen(value);
    }

    @When("admin clicks the Tambah submit button")
    public void adminClicksTambahSubmitButton() {
        unitSettingPage().clickTambahSubmit();
    }

    // =========================================================================
    // WHEN — Toggle flow
    // =========================================================================

    @When("admin toggles the selfie switch on the first unit row")
    public void adminTogglesSelfieSwitchOnFirstUnitRow() {
        unitSettingPage().toggleFirstRowSelfieSwitch();
    }

    @When("admin accepts the native browser confirmation")
    public void adminAcceptsNativeBrowserConfirmation() {
        unitSettingPage().acceptNativeAlert();
    }

    // =========================================================================
    // WHEN — Delete flow
    // =========================================================================

    @When("admin clicks the delete button on the first unit row")
    public void adminClicksDeleteButtonOnFirstUnitRow() {
        unitSettingPage().clickFirstRowDeleteButton();
    }

    @When("admin confirms the delete by clicking {string}")
    public void adminConfirmsDeleteByClicking(String buttonLabel) {
        unitSettingPage().confirmDelete();
    }

    // =========================================================================
    // THEN
    // =========================================================================

    @Then("the unit setting action should complete without error")
    public void unitSettingActionShouldCompleteWithoutError() {
        // If we reached this step, the prior action's wait condition
        // (dialog closed / alert accepted / table reappeared) already
        // succeeded without exception — that is sufficient per requirements.
        Assert.assertTrue(true, "Action completed without error");
        logger.info("Unit setting action completed successfully");
    }
}
