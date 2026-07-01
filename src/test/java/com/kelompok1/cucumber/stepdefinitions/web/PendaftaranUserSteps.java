package com.kelompok1.cucumber.stepdefinitions.web;

import com.kelompok1.cucumber.pages.web.PendaftaranUserPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class PendaftaranUserSteps {

    private PendaftaranUserPage page() {
        return new PendaftaranUserPage();
    }

    @And("admin navigates to Pendaftaran User page")
    public void adminNavigatesToPendaftaranUserPage() {
        page().navigateToPendaftaranUser();
        Assert.assertTrue(page().isPendaftaranUserPageDisplayed(),
            "Admin should be on the Pendaftaran User registration page. Actual URL: " + page().getCurrentUrl());
    }

    @When("admin uploads employee photo from {string}")
    public void adminUploadsEmployeePhotoFrom(String resourcePath) {
        page().uploadPhoto(resourcePath);
    }

    @When("admin enters NIK with {string}")
    public void adminEntersNikWith(String nik) {
        page().enterNik(nik);
    }

    @When("admin enters nama karyawan with {string}")
    public void adminEntersNamaKaryawanWith(String nama) {
        page().enterNamaKaryawan(nama);
    }

    @When("admin enters registration email with {string}")
    public void adminEntersRegistrationEmailWith(String email) {
        page().enterEmail(email);
    }

    @When("admin enters registration password with {string}")
    public void adminEntersRegistrationPasswordWith(String password) {
        page().enterPassword(password);
    }

    @When("admin selects divisi with {string}")
    public void adminSelectsDivisiWith(String text) {
        page().selectDivisi(text);
    }

    @When("admin selects unit with {string}")
    public void adminSelectsUnitWith(String text) {
        page().selectUnit(text);
    }

    @When("admin selects posisi kerja with {string}")
    public void adminSelectsPosisiKerjaWith(String text) {
        page().selectPosisiKerja(text);
    }

    @When("admin selects jabatan with {string}")
    public void adminSelectsJabatanWith(String text) {
        page().selectJabatan(text);
    }

    @When("admin selects atasan with {string}")
    public void adminSelectsAtasanWith(String text) {
        page().selectAtasan(text);
    }

    @When("admin selects atasan client v3 with {string}")
    public void adminSelectsAtasanClientV3With(String text) {
        page().selectAtasanClientV3(text);
    }

    @When("admin selects tipe kontrak with {string}")
    public void adminSelectsTipeKontrakWith(String text) {
        page().selectTipeKontrak(text);
    }

    @When("admin selects lokasi kerja with {string}")
    public void adminSelectsLokasiKerjaWith(String text) {
        page().selectLokasiKerja(text);
    }

    @When("admin selects tipe shift with {string}")
    public void adminSelectsTipeShiftWith(String text) {
        page().selectTipeShift(text);
    }

    @When("admin selects jadwal kerja with {string}")
    public void adminSelectsJadwalKerjaWith(String text) {
        page().selectJadwalKerja(text);
    }

    @When("admin selects selfie with {string}")
    public void adminSelectsSelfieWith(String text) {
        page().selectSelfie(text);
    }

    @When("admin enters jumlah cuti with {string}")
    public void adminEntersJumlahCutiWith(String jumlah) {
        page().enterJumlahCuti(jumlah);
    }

    @When("admin clicks the submit button")
    public void adminClicksTheSubmitButton() {
        page().clickSubmit();
    }

    @Then("admin should be redirected to the user management list page")
    public void adminShouldBeRedirectedToTheUserManagementListPage() {
        Assert.assertTrue(page().isRedirectedToUserList(),
            "Expected redirect to /management/user after successful registration");
    }

    @Then("admin should remain on the pendaftaran user page")
    public void adminShouldRemainOnThePendaftaranUserPage() {
        Assert.assertTrue(page().isOnPendaftaranUserPage(),
            "Expected to remain on /management/user-v2/register because required fields were empty");
    }
}
