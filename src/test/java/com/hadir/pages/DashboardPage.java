package com.hadir.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By profileMenu = By.xpath("//*[contains(text(), 'Hi, Admin') or contains(text(), 'Hi, admin') or contains(text(), 'Admin Hadir')]");
    private By logoutButton = By.xpath("//*[text()='Logout' or contains(text(), 'Logout')]");
    private By changePasswordButton = By.xpath("//*[text()='Ubah Password' or contains(text(), 'Ubah Password')]");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToDashboard() {
        driver.get("https://magang.dikahadir.com/dashboards/dashboard");
        wait.until(ExpectedConditions.urlContains("dashboards/dashboard"));
    }

    public String getMetricValue(String cardTitle) {
        // Find the card container that has the cardTitle text, then get the value inside MuiCardContent-root
        By locator = By.xpath("//*[text()='" + cardTitle + "']/ancestor::div[contains(@class, 'MuiCard-root')]//div[contains(@class, 'MuiCardContent-root')]//p");
        WebElement valElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return valElement.getText().trim();
    }

    public boolean isMetricVisible(String cardTitle) {
        try {
            By locator = By.xpath("//*[text()='" + cardTitle + "']");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickProfileMenu() {
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(profileMenu));
        // Use JS click if needed to ensure dropdown expands
        try {
            menu.click();
        } catch (Exception e) {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", menu);
        }
    }

    public boolean isLogoutOptionVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isChangePasswordOptionVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(changePasswordButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLogout() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        btn.click();
    }
}
