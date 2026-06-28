package com.kelompok1.cucumber.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility to capture screenshots and organize them into date-based directories.
 */
public class ScreenshotUtil {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtil.class);

    public static void takeScreenshot(WebDriver driver, String scenarioName, String status) {
        if (driver == null) {
            logger.warn("WebDriver is null, skipping screenshot capture");
            return;
        }

        try {
            // YYYY-MM-DD folder format
            String dateFolder = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String timestamp = new SimpleDateFormat("HH-mm-ss").format(new Date());
            
            File destDir = new File("screenshots/" + dateFolder);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            
            // Clean scenario name of invalid filename characters
            String safeScenarioName = scenarioName.replaceAll("[^a-zA-Z0-9_-]", "_");
            String fileName = String.format("%s_%s_%s.png", safeScenarioName, status, timestamp);
            File destFile = new File(destDir, fileName);
            
            Files.copy(srcFile.toPath(), destFile.toPath());
            logger.info("Screenshot saved successfully: {}", destFile.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Failed to capture and save screenshot: {}", e.getMessage(), e);
        }
    }
}
