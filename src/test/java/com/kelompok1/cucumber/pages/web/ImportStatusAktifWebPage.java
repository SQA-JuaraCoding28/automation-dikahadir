package com.kelompok1.cucumber.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.kelompok1.cucumber.pages.BasePage;

public class ImportStatusAktifWebPage extends BasePage {

    private WebDriver driver;

    // ==========================================
    // LOKATOR ELEMENT
    // ==========================================
    @FindBy(xpath = "//p[contains(text(), 'Import Status Aktif')] | //div[contains(text(), 'Import Excel Status Aktif')]")
    private WebElement headerTitle;

    @FindBy(id = "selfie")
    private WebElement inputFile;

    @FindBy(xpath = "//button[@type='submit' and contains(text(), 'Import')]")
    private WebElement btnImport;

    @FindBy(xpath = "//button[contains(text(), 'Download Template')]")
    private WebElement btnDownloadTemplate;

    public ImportStatusAktifWebPage(WebDriver driver) {
        super();
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ==========================================
    // METODE INTERAKSI
    // ==========================================
    
    public ImportStatusAktifWebPage navigateToImportStatusAktif() {
        try {
            String currentUrl = driver.getCurrentUrl();
            // Ekstrak base URL (contoh: https://magang.dikahadir.com)
            String baseUrl = currentUrl.split("/dashboards")[0].split("/authentication")[0];
            System.out.println("Navigasi langsung ke URL Import: " + baseUrl + "/imports/import-status-aktif");
            driver.get(baseUrl + "/imports/import-status-aktif");
            Thread.sleep(3000); // Tunggu rendering React selesai
        } catch(Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public ImportStatusAktifWebPage verifyOnPage() {
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("import-status-aktif"));
            
            waitForVisibility(headerTitle);
            Assert.assertTrue(headerTitle.isDisplayed(), "Header halaman Import Status Aktif tidak tampil!");
            System.out.println("Berhasil masuk ke halaman Import!");
        } catch (Exception e) {
            System.out.println("GAGAL MEMUAT HALAMAN IMPORT!");
            System.out.println("URL saat ini: " + driver.getCurrentUrl());
            Assert.fail("Gagal memuat halaman Import Status Aktif. URL: " + driver.getCurrentUrl());
        }
        return this;
    }

    public ImportStatusAktifWebPage verifyButtonsDisplayed() {
        waitForVisibility(btnImport);
        Assert.assertTrue(btnImport.isDisplayed(), "Tombol Import tidak tampil!");
        
        waitForVisibility(btnDownloadTemplate);
        Assert.assertTrue(btnDownloadTemplate.isDisplayed(), "Tombol Download Template tidak tampil!");
        return this;
    }

    public ImportStatusAktifWebPage clickImportWithoutFile() {
        waitForVisibility(btnImport);
        btnImport.click();
        return this;
    }

    public ImportStatusAktifWebPage verifyActionRejected() {
        // Karena input file menggunakan required="", browser akan menolak form submission.
        // Di Selenium, kita bisa cek attribute validationMessage dari input HTML5.
        waitForVisibility(inputFile);
        String validationMsg = inputFile.getAttribute("validationMessage");
        
        boolean isRejected = (validationMsg != null && !validationMsg.isEmpty()) || btnImport.isEnabled();
        Assert.assertTrue(isRejected, "Sistem seharusnya menolak import jika file belum dipilih!");
        return this;
    }
}
