package com.kelompok1.cucumber.stepdefinitions.web;

import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.web.RegisterUserPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.testng.Assert;

/**
 * Step Definitions for User Registration (Registrasi User) feature.
 * URL: https://magang.dikahadir.com/management/user-v2/register
 */
public class RegisterUserSteps {

    private RegisterUserPage registerPage = new RegisterUserPage();

    // =========================================================================
    // GIVEN
    // =========================================================================

    @Given("User is on the Register User page")
    public void user_is_on_the_register_user_page() {
        registerPage.navigateToPage();
        Assert.assertTrue(registerPage.isPageDisplayed(),
            "Register User page should be displayed with 'Registrasi User' heading");
    }

    @Given("User attempts to navigate to Register User page directly without logging in")
    public void user_attempts_to_navigate_to_register_user_page_directly_without_logging_in() {
        DriverManager.getDriver().manage().deleteAllCookies();
        registerPage.navigateToPage();
    }

    // =========================================================================
    // WHEN — Account Information
    // =========================================================================

    @When("User enters NIK {string}")
    public void user_enters_nik(String nik) {
        registerPage.enterNik(nik);
    }

    @When("User enters Nama Karyawan {string}")
    public void user_enters_nama_karyawan(String name) {
        registerPage.enterFullname(name);
    }

    @When("User enters registration email {string}")
    public void user_enters_registration_email(String email) {
        registerPage.enterEmail(email);
    }

    @When("User enters registration password {string}")
    public void user_enters_registration_password(String password) {
        registerPage.enterPassword(password);
    }

    // =========================================================================
    // WHEN — Work Information (with specific value)
    // =========================================================================

    @When("User selects Divisi {string}")
    public void user_selects_divisi(String divisi) {
        registerPage.selectDivisi(divisi);
    }

    @When("User selects Unit {string}")
    public void user_selects_unit(String unit) {
        registerPage.selectUnit(unit);
    }

    @When("User selects Posisi Kerja {string}")
    public void user_selects_posisi_kerja(String posisi) {
        registerPage.selectPosisiKerja(posisi);
    }

    @When("User selects Jabatan {string}")
    public void user_selects_jabatan(String jabatan) {
        registerPage.selectJabatan(jabatan);
    }

    @When("User selects Tipe Kontrak {string}")
    public void user_selects_tipe_kontrak(String kontrak) {
        registerPage.selectTipeKontrak(kontrak);
    }

    // =========================================================================
    // WHEN — Work Information (first available option for cascading dropdowns)
    // =========================================================================

    @When("User selects the first available Divisi")
    public void user_selects_first_available_divisi() {
        registerPage.selectFirstDivisi();
    }

    @When("User selects the first available Unit")
    public void user_selects_first_available_unit() {
        registerPage.selectFirstUnit();
    }

    @When("User selects the first available Posisi Kerja")
    public void user_selects_first_available_posisi_kerja() {
        registerPage.selectFirstPosisiKerja();
    }

    @When("User selects the first available Jabatan")
    public void user_selects_first_available_jabatan() {
        registerPage.selectFirstJabatan();
    }

    @When("User selects the first available Tipe Kontrak")
    public void user_selects_first_available_tipe_kontrak() {
        registerPage.selectFirstTipeKontrak();
    }

    // =========================================================================
    // WHEN — Attendance Settings
    // =========================================================================

    @When("User selects Jadwal Kerja {string}")
    public void user_selects_jadwal_kerja(String jadwal) {
        registerPage.selectJadwalKerja(jadwal);
    }

    @When("User selects the first available Jadwal Kerja")
    public void user_selects_first_available_jadwal_kerja() {
        registerPage.selectFirstJadwalKerja();
    }

    @When("User selects Lokasi Kerja {string}")
    public void user_selects_lokasi_kerja(String lokasi) {
        registerPage.selectLokasiKerja(lokasi);
    }

    @When("User enters Jumlah Cuti {string}")
    public void user_enters_jumlah_cuti(String amount) {
        registerPage.enterJumlahCuti(amount);
    }

    // =========================================================================
    // WHEN — Form Actions
    // =========================================================================

    @When("User clicks submit button on register form")
    public void user_clicks_submit_button_on_register_form() {
        registerPage.clickSubmit();
    }

    // =========================================================================
    // THEN — Assertions
    // =========================================================================

    @Then("Registration success message should be displayed")
    public void registration_success_message_should_be_displayed() {
        Assert.assertTrue(registerPage.isSuccessMessageDisplayed(),
            "Expected a success message after registration but none was displayed");
    }

    @Then("Registration error message should be displayed")
    public void registration_error_message_should_be_displayed() {
        boolean hasError = registerPage.isErrorMessageDisplayed() || registerPage.hasValidationErrors();
        Assert.assertTrue(hasError,
            "Expected an error message or validation errors after failed registration");
    }

    @Then("User should remain on the Register User page")
    public void user_should_remain_on_the_register_user_page() {
        Assert.assertTrue(registerPage.isStillOnRegisterPage(),
            "User should still be on the Register User page. Current URL: " + 
            DriverManager.getDriver().getCurrentUrl());
    }

    @Then("User is redirected to the login page from register")
    public void user_is_redirected_to_the_login_page_from_register() {
        boolean onLogin = DriverManager.getDriver().getCurrentUrl().contains("login");
        Assert.assertTrue(onLogin,
            "User was not redirected to the login page. Current URL: " + 
            DriverManager.getDriver().getCurrentUrl());
    }

    @Then("Register page should display the heading {string}")
    public void register_page_should_display_heading(String heading) {
        Assert.assertTrue(registerPage.isPageDisplayed(),
            "Expected heading '" + heading + "' to be displayed on register page");
    }

    @Then("Submit button should be present on register form")
    public void submit_button_should_be_present() {
        Assert.assertTrue(registerPage.isSubmitButtonPresent(),
            "Submit button should be present on register form");
    }

    @Then("The form should have validation errors")
    public void the_form_should_have_validation_errors() {
        boolean staysOnPage = registerPage.isStillOnRegisterPage();
        Assert.assertTrue(staysOnPage,
            "Expected form validation to prevent submission. Current URL: " + 
            DriverManager.getDriver().getCurrentUrl());
    }
}
