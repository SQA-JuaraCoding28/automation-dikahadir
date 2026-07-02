package com.kelompok1.cucumber.pages.mobile;

import org.openqa.selenium.By;
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

public class HistoryAbsensiMobilePage extends BasePage {

    private WebDriver driver;
    private WebDriverWait wait;

    public HistoryAbsensiMobilePage(WebDriver driver) {
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
            Files.write(Paths.get("history_absensi_dump.html"), driver.getPageSource().getBytes());
            System.out.println("HTML DUMPED to history_absensi_dump.html");
        } catch(Exception e) {}
    }

    public HistoryAbsensiMobilePage navigateToHistoryAbsensi() {
        String url = "https://magang.dikahadir.com/apps/absent/activity";
        driver.get(url);
        sleep(3000);
        dumpHtml();
        return this;
    }

    public HistoryAbsensiMobilePage verifyCardsDisplayed() {
        try {
            // Coba temukan daftar card absensi. Kita gunakan XPath umum yang bisa menjangkau div berulang.
            // Dari screenshot, bisa jadi ada teks "Check In", "Work From", dsb.
            WebElement historyContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Check In') or contains(text(), 'Work From')]")));
            Assert.assertTrue(historyContainer.isDisplayed(), "Card History Absensi tidak muncul");
            
            // Verifikasi minimal ada 1 card
            List<WebElement> cards = driver.findElements(By.xpath("//*[contains(text(), 'Check In') or contains(text(), 'Sakit')]"));
            Assert.assertTrue(cards.size() > 0, "Daftar History kosong, seharusnya ada minimal 1 absensi.");
        } catch (Exception e) {
            Assert.fail("Gagal memverifikasi card history absensi: " + e.getMessage());
        }
        return this;
    }
}
