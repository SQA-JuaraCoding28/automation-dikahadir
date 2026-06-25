package com.kelompok1.cucumber.core;

import org.openqa.selenium.WebDriver;

/**
 * ThreadLocal WebDriver manager for safe parallel test execution.
 * Each thread gets its own WebDriver instance, preventing cross-scenario interference.
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    public static void quit() {
        WebDriver current = driver.get();
        if (current != null) {
            current.quit();
            driver.remove(); // prevent memory leak in thread pools
        }
    }
}
