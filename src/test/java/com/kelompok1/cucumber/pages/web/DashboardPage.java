package com.kelompok1.cucumber.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.kelompok1.cucumber.pages.BasePage;

public class DashboardPage extends BasePage {

    // Locators
    private final By profileMenu = By.xpath("//*[contains(text(), 'Hi, Admin') or contains(text(), 'Hi, admin') or contains(text(), 'Admin Hadir')]");
    private final By logoutButton = By.xpath("//*[text()='Logout' or contains(text(), 'Logout')]");
    private final By changePasswordButton = By.xpath("//*[text()='Ubah Password' or contains(text(), 'Ubah Password')]");

    public DashboardPage() {
        super();
    }

    public void navigateToDashboard() {
        navigateTo("https://magang.dikahadir.com/dashboards/dashboard");
        waitForUrlContains("dashboards/dashboard");
    }

    public String getMetricValue(String cardTitle) {
        // Find the card container that has the cardTitle text, then get the value inside MuiCardContent-root
        By locator = By.xpath("//*[text()='" + cardTitle + "']/ancestor::div[contains(@class, 'MuiCard-root')]//div[contains(@class, 'MuiCardContent-root')]//p");
        return getText(locator).trim();
    }

    public boolean isMetricVisible(String cardTitle) {
        By locator = By.xpath("//*[text()='" + cardTitle + "']");
        return isDisplayed(locator);
    }

    public void clickProfileMenu() {
        WebElement menu = findElement(profileMenu);
        try {
            click(profileMenu);
        } catch (Exception e) {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", menu);
        }
    }

    public boolean isLogoutOptionVisible() {
        return isDisplayed(logoutButton);
    }

    public boolean isChangePasswordOptionVisible() {
        return isDisplayed(changePasswordButton);
    }

    public void clickLogout() {
        click(logoutButton);
    }

    public boolean waitForUrlToContain(String partialUrl) {
        return waitForUrlContains(partialUrl);
    }
}
