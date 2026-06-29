package com.kelompok1.cucumber.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;

import com.kelompok1.cucumber.core.ConfigReader;
import com.kelompok1.cucumber.pages.BasePage;

public class PendaftaranSendiriWebPage extends BasePage {

    private WebDriver driver;
    private WebDriverWait wait;

    public PendaftaranSendiriWebPage(WebDriver driver) {
        super();
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    private void sleep(int millis) {
        try { Thread.sleep(millis); } catch (Exception e) {}
    }

    public PendaftaranSendiriWebPage navigateToPendaftaran() {
        String url = "https://magang.dikahadir.com/management/self-registration";
        driver.get(url);
        sleep(2000);
        return this;
    }

    public PendaftaranSendiriWebPage verifyPageTitle(String expectedTitle) {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), '" + expectedTitle + "')]")));
            Assert.assertTrue(title.isDisplayed(), "Judul halaman '" + expectedTitle + "' tidak ditemukan.");
        } catch (Exception e) {
            Assert.fail("Gagal memverifikasi judul halaman: " + e.getMessage());
        }
        return this;
    }

    public PendaftaranSendiriWebPage verifyTableDisplayed() {
        try {
            WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));
            Assert.assertTrue(table.isDisplayed(), "Tabel data pendaftaran tidak muncul");
            
            // Verifikasi bahwa ada minimal satu baris data (selain header)
            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
            Assert.assertTrue(rows.size() > 0, "Tabel kosong, seharusnya ada data.");
        } catch (Exception e) {
            Assert.fail("Gagal memverifikasi tabel pendaftaran: " + e.getMessage());
        }
        return this;
    }

    public PendaftaranSendiriWebPage searchData(String query) {
        try {
            WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='search' or @type='search']")));
            searchInput.clear();
            searchInput.sendKeys(query);
            
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' or contains(., 'Search')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchBtn);
            sleep(2000); // Tunggu hasil pencarian dimuat
        } catch (Exception e) {
            Assert.fail("Gagal mencari data: " + e.getMessage());
        }
        return this;
    }

    public PendaftaranSendiriWebPage verifyEmptyTable() {
        try {
            // Jika kosong, tabel mungkin tidak punya row di tbody, atau muncul pesan "Data tidak ditemukan" / "No rows"
            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
            
            boolean isEmpty = rows.isEmpty();
            if (!isEmpty && rows.size() == 1) {
                String text = rows.get(0).getText().toLowerCase();
                if (text.contains("tidak ada") || text.contains("kosong") || text.contains("no data") || text.contains("not found")) {
                    isEmpty = true;
                }
            }
            Assert.assertTrue(isEmpty, "Tabel seharusnya kosong tapi masih ada data!");
        } catch (Exception e) {
            Assert.fail("Gagal memverifikasi tabel kosong: " + e.getMessage());
        }
        return this;
    }
}
