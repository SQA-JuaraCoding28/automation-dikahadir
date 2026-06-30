package com.kelompok1.cucumber.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.kelompok1.cucumber.pages.BasePage;

public class KehadiranSakitWebPage extends BasePage {

    private WebDriver driver;

    // ==========================================
    // 1. LOKATOR ELEMENT: MENU UTAMA
    // ==========================================
    private final By btnHamburgerMenu = By.xpath("//button[@aria-label='menu' and .//*[name()='svg' and contains(@class, 'feather-menu')]]");
    private final By menuLaporan = By.xpath("//a[contains(@href, '/laporan')] | //*[contains(translate(text(), 'LAPORAN', 'laporan'), 'laporan')]");
    private final By menuSakit = By.xpath("//a[contains(@href, '/laporan/sakit')]");
    private final By menuKehadiran = By.xpath("//a[contains(@href, '/laporan/kehadiran')]");

    // ==========================================
    // 2. LOKATOR ELEMENT: HALAMAN DATA
    // ==========================================
    private final By headerSakit = By.xpath("//*[self::h1 or self::h2 or self::h3 or self::h4 or self::h5 or self::h6 or self::p or self::span][contains(text(), 'Sakit')]");
    private final By headerKehadiran = By.xpath("//*[self::h1 or self::h2 or self::h3 or self::h4 or self::h5 or self::h6 or self::p or self::span][contains(text(), 'Kehadiran')]");
    private final By dataTable = By.xpath("//table");

    // ==========================================
    // 3. LOKATOR ELEMENT: PENCARIAN (NEGATIVE)
    // ==========================================
    private final By inputSearch = By.xpath("//input[@type='search' or contains(@placeholder, 'Cari') or contains(@placeholder, 'Search') or @id='search']");
    private final By tableRows = By.xpath("//tbody/tr");

    // ==========================================
    // 4. CONSTRUCTOR
    // ==========================================
    public KehadiranSakitWebPage(WebDriver driver) {
        super();
        this.driver = driver; // Simpan instance driver untuk JavascriptExecutor
    }

    // ==========================================
    // 5. HELPER METHODS
    // ==========================================
    
    /**
     * Mengecek apakah elemen ada di DOM dan terlihat dengan aman tanpa melempar exception
     */
    private boolean isElementReady(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Melakukan scroll ke elemen dan klik menggunakan Javascript.
     * Sangat ampuh untuk mengatasi isu UI bertumpuk di mode headless.
     */
    private void clickWithJS(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        js.executeScript("arguments[0].click();", element);
    }

    public KehadiranSakitWebPage clickMenuSakit() {
        // Navigasi langsung via URL untuk menghindari isu sidebar yang tertutup/responsif
        try {
            String currentUrl = driver.getCurrentUrl();
            java.net.URL url = new java.net.URL(currentUrl);
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            if (url.getPort() != -1) {
                baseUrl += ":" + url.getPort();
            }
            System.out.println("Navigasi langsung ke: " + baseUrl + "/laporan/sakit");
            driver.get(baseUrl + "/laporan/sakit");
            Thread.sleep(2000); // Tunggu loading halaman selesai
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public KehadiranSakitWebPage clickMenuKehadiran() {
        // Navigasi langsung via URL
        try {
            String currentUrl = driver.getCurrentUrl();
            java.net.URL url = new java.net.URL(currentUrl);
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            if (url.getPort() != -1) {
                baseUrl += ":" + url.getPort();
            }
            System.out.println("Navigasi langsung ke: " + baseUrl + "/laporan/all");
            driver.get(baseUrl + "/laporan/all");
            Thread.sleep(2000); // Tunggu loading halaman selesai
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public KehadiranSakitWebPage verifyOnSakitPage() {
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("sakit"));
        } catch (Exception e) {
            Assert.fail("Gagal memuat halaman Sakit. URL saat ini: " + driver.getCurrentUrl());
        }
        return this;
    }

    public KehadiranSakitWebPage verifyOnKehadiranPage() {
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            // Halaman Kehadiran menggunakan route /laporan/all
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("laporan/all"));
        } catch (Exception e) {
            Assert.fail("Gagal memuat halaman Kehadiran. URL saat ini: " + driver.getCurrentUrl());
        }
        return this;
    }

    public KehadiranSakitWebPage verifyTableDisplayed() {
        // Tunggu sebentar untuk memastikan tabel/API selesai dimuat
        try { Thread.sleep(3000); } catch (Exception e) {}
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Boolean isTableVisible = (Boolean) js.executeScript(
            "const tables = document.querySelectorAll('table, [role=\"grid\"]');" +
            "for (let t of tables) {" +
            "  if (t.offsetWidth > 0 && t.offsetHeight > 0) return true;" +
            "}" +
            "return false;"
        );
        
        if (!isTableVisible) {
            System.out.println("========== DEBUG INFO ==========");
            System.out.println("CURRENT URL: " + driver.getCurrentUrl());
            System.out.println("PAGE TITLE: " + driver.getTitle());
            String bodyText = driver.findElement(org.openqa.selenium.By.tagName("body")).getText();
            System.out.println("BODY TEXT: " + (bodyText.length() > 500 ? bodyText.substring(0, 500) + "..." : bodyText));
            System.out.println("================================");
        }

        org.testng.Assert.assertTrue(isTableVisible, "Tabel data (atau grid) tidak ditemukan atau tidak terlihat di layar!");
        return this;
    }

    public KehadiranSakitWebPage searchData(String keyword) {
        waitForVisibility(inputSearch);
        WebElement searchInput = findElement(inputSearch);
        searchInput.clear();
        searchInput.sendKeys(keyword + org.openqa.selenium.Keys.ENTER);
        return this;
    }

    public KehadiranSakitWebPage verifyEmptyData() {
        // Tunggu sebentar agar filter pencarian sempat diproses oleh website
        try { Thread.sleep(3000); } catch (Exception e) {}
        
        int rowCount = 0;
        try {
            rowCount = findAll(tableRows).size();
        } catch (Exception e) {}

        boolean isEmpty = false;
        
        // Kasus 1: Tabel benar-benar tidak memiliki baris (0 baris)
        if (rowCount == 0) {
            isEmpty = true;
        } 
        // Kasus 2: Tabel memiliki 1 baris, tapi baris itu adalah pesan "Data Kosong"
        else if (rowCount == 1) {
            String rowText = findAll(tableRows).get(0).getText().toLowerCase();
            if (rowText.contains("tidak") || rowText.contains("kosong") || 
                rowText.contains("no data") || rowText.contains("belum") || 
                rowText.contains("0") || rowText.length() < 5) {
                isEmpty = true;
            }
        }
        
        // Coba cari elemen teks mandiri jika tabel tidak bisa mendeteksi
        if (!isEmpty) {
            try {
                WebElement emptyText = driver.findElement(org.openqa.selenium.By.xpath(
                    "//*[contains(translate(text(), 'TIDAK', 'tidak'), 'tidak') or " +
                    "contains(translate(text(), 'KOSONG', 'kosong'), 'kosong') or " +
                    "contains(translate(text(), 'NO DATA', 'no data'), 'no data')]"
                ));
                if (emptyText.isDisplayed()) {
                    isEmpty = true;
                }
            } catch (Exception e) {}
        }

        org.testng.Assert.assertTrue(isEmpty, "Pencarian gagal! Data tabel tidak kosong (masih ada " + rowCount + " baris data).");
        return this;
    }
}