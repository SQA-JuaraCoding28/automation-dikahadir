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

public class SakitMobilePage extends BasePage {

    private WebDriver driver;
    private WebDriverWait wait;

    public SakitMobilePage(WebDriver driver) {
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
            Files.write(Paths.get("sakit_mobile_dump.html"), driver.getPageSource().getBytes());
            System.out.println("HTML DUMPED to sakit_mobile_dump.html");
        } catch(Exception e) {}
    }

    public SakitMobilePage navigateToSakit() {
        String url = "https://magang.dikahadir.com/apps/absent/sick";
        driver.get(url);
        sleep(3000);
        dumpHtml();
        return this;
    }

    public SakitMobilePage verifyCardsDisplayed() {
        try {
            // Coba temukan daftar card absensi sakit. Kita gunakan XPath umum untuk mendeteksi elemen khas halaman ini.
            WebElement headerOrCard = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'List Request Sakit') or contains(text(), 'Dari tanggal')]")));
            Assert.assertTrue(headerOrCard.isDisplayed(), "Halaman list request sakit tidak muncul");
            
            // Verifikasi minimal ada tombol Ajukan Sakit atau card
            List<WebElement> cards = driver.findElements(By.xpath("//*[contains(text(), 'Sampai Tanggal') or contains(text(), 'Ajukan Sakit')]"));
            Assert.assertTrue(cards.size() > 0, "Daftar Sakit kosong atau tidak valid.");
        } catch (Exception e) {
            Assert.fail("Gagal memverifikasi list request sakit: " + e.getMessage());
        }
        return this;
    }
}
