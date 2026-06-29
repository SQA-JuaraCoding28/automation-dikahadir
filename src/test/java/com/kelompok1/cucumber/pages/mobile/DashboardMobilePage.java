package com.kelompok1.cucumber.pages.mobile;

import org.openqa.selenium.By;

import com.kelompok1.cucumber.core.ConfigReader;
import com.kelompok1.cucumber.pages.BasePage;

/**
 * Page Object for Mobile Dashboard.
 * URL: https://magang.dikahadir.com/apps/absent
 *
 * Navigation hub for other test flows (Absensi, Cuti, Lembur, etc.).
 */
public class DashboardMobilePage extends BasePage {

    // ── Identity / Verification ──
    private final By greetingText = By.xpath("//p[contains(text(),'Hai,')]");

    // ── Floating Action Button ──
    private final By absenMasukButton = By.xpath("//button[contains(.,'Absen Masuk')]");

    // ── Menu Items ──
    private final By menuAbsensi        = By.xpath("//a[contains(@class,'user__menu__item')]//img[@alt='Absensi']");
    private final By menuKoreksiAbsen   = By.xpath("//a[contains(@class,'user__menu__item')]//img[@alt='Koreksi Absen']");
    private final By menuIzin           = By.xpath("//a[contains(@class,'user__menu__item')]//img[@alt='Izin']");
    private final By menuLembur         = By.xpath("//a[contains(@class,'user__menu__item')]//img[@alt='Lembur']");
    private final By menuCuti           = By.xpath("//a[contains(@class,'user__menu__item')]//img[@alt='Cuti']");
    private final By menuSakit          = By.xpath("//a[contains(@class,'user__menu__item')]//img[@alt='Sakit']");
    private final By menuDownloadReports = By.xpath("//a[contains(@class,'user__menu__item')]//img[@alt='Download Reports']");
    private final By menuStaff          = By.xpath("//a[contains(@class,'user__menu__item')]//img[@alt='Staff']");

    public DashboardMobilePage() {
        super();
    }

    public boolean isOnDashboard() {
        return waitForUrlToBe(ConfigReader.getProperty("dashboard.url"));
    }

    public String getGreetingText() {
        return getText(greetingText);
    }

    // =========================================================================
    // ABSEN MASUK — Floating Action Button
    // =========================================================================

    public boolean isAbsenMasukDisplayed() {
        return isDisplayed(absenMasukButton);
    }

    public void clickAbsenMasuk() {
        click(absenMasukButton);
    }

    public String getAbsenMasukText() {
        return getText(absenMasukButton);
    }

    // =========================================================================
    // MENU NAVIGATION
    // =========================================================================

    public void goToAbsensi()        { click(menuAbsensi); }
    public void goToKoreksiAbsen()   { click(menuKoreksiAbsen); }
    public void goToIzin()           { click(menuIzin); }
    public void goToLembur()         { click(menuLembur); }
    public void goToCuti()           { click(menuCuti); }
    public void goToSakit()          { click(menuSakit); }
    public void goToDownloadReports(){ click(menuDownloadReports); }
    public void goToStaff()          { click(menuStaff); }

}
