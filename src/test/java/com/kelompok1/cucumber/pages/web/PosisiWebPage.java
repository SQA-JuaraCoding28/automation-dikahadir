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
import java.nio.file.Files;
import java.nio.file.Paths;

import com.kelompok1.cucumber.pages.BasePage;

public class PosisiWebPage extends BasePage {

    private WebDriver driver;
    private WebDriverWait wait;

    public PosisiWebPage(WebDriver driver) {
        super();
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    private void sleep(int millis) {
        try { Thread.sleep(millis); } catch (Exception e) {}
    }
    
    private void dumpHtml() {
        try {
            Files.write(Paths.get("posisi_dump.html"), driver.getPageSource().getBytes());
            System.out.println("HTML DUMPED to posisi_dump.html");
        } catch(Exception e) {}
    }

    public PosisiWebPage navigateToPosisi() {
        String url = "https://magang.dikahadir.com/management/position";
        driver.get(url);
        sleep(2000);
        dumpHtml();
        return this;
    }

    public PosisiWebPage verifyTableDisplayed() {
        try {
            WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));
            Assert.assertTrue(table.isDisplayed(), "Tabel data posisi tidak muncul");
            
            // Verifikasi minimal satu baris (selain header)
            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
            Assert.assertTrue(rows.size() > 0, "Tabel kosong, seharusnya ada data.");
        } catch (Exception e) {
            Assert.fail("Gagal memverifikasi tabel posisi: " + e.getMessage());
        }
        return this;
    }

    public PosisiWebPage searchData(String query) {
        try {
            // Kita coba tebak inputnya id='search' atau typenya search
            WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='search' or @type='search' or contains(@placeholder, 'Search') or contains(@placeholder, 'Cari')]")));
            searchInput.clear();
            searchInput.sendKeys(query);
            
            // Kita coba tebak tombol searchnya
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' or contains(., 'Search') or contains(., 'Cari')] | //button//svg[@data-testid='SearchIcon']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchBtn);
            sleep(2000);
        } catch (Exception e) {
            Assert.fail("Gagal mencari data posisi: " + e.getMessage());
        }
        return this;
    }

    public PosisiWebPage verifyEmptyTable() {
        try {
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
            Assert.fail("Gagal memverifikasi tabel posisi kosong: " + e.getMessage());
        }
        return this;
    }
}
