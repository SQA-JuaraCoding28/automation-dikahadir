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

public class ShiftingWebPage extends BasePage {

    private WebDriver driver;
    private WebDriverWait wait;

    public ShiftingWebPage(WebDriver driver) {
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
            Files.write(Paths.get("shifting_dump.html"), driver.getPageSource().getBytes());
            System.out.println("HTML DUMPED to shifting_dump.html");
        } catch(Exception e) {}
    }

    public ShiftingWebPage navigateToShifting() {
        String url = "https://magang.dikahadir.com/management/shifting";
        driver.get(url);
        sleep(2000);
        dumpHtml();
        return this;
    }

    public ShiftingWebPage verifyTableDisplayed() {
        try {
            WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));
            Assert.assertTrue(table.isDisplayed(), "Tabel data shifting tidak muncul");
            
            // Verifikasi minimal satu baris (selain header)
            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
            Assert.assertTrue(rows.size() > 0, "Tabel kosong, seharusnya ada data.");
        } catch (Exception e) {
            Assert.fail("Gagal memverifikasi tabel shifting: " + e.getMessage());
        }
        return this;
    }

    public ShiftingWebPage searchData(String query) {
        try {
            WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='search' or @type='search' or contains(@placeholder, 'Search') or contains(@placeholder, 'Cari')]")));
            searchInput.clear();
            searchInput.sendKeys(query);
            
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' or contains(., 'Search') or contains(., 'Cari')] | //button//svg[@data-testid='SearchIcon']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchBtn);
            sleep(2000);
        } catch (Exception e) {
            Assert.fail("Gagal mencari data shifting: " + e.getMessage());
        }
        return this;
    }

    public ShiftingWebPage verifyEmptyTable() {
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
            Assert.fail("Gagal memverifikasi tabel shifting kosong: " + e.getMessage());
        }
        return this;
    }
}
