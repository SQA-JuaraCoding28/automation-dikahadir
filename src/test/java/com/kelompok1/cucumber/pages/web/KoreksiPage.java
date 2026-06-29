package com.kelompok1.cucumber.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.kelompok1.cucumber.pages.BasePage;
import java.util.List;

public class KoreksiPage extends BasePage {

    // Locators
    private final By searchInput = By.id("search");
    private final By startDateInput = By.xpath("//input[@placeholder='Start Date']");
    private final By endDateInput = By.xpath("//input[@placeholder='End Date']");
    private final By searchButton = By.xpath("//button[text()='Search' or contains(text(), 'Search')]");
    private final By resetButton = By.xpath("//button[text()='Reset' or contains(text(), 'Reset')]");
    
    // Table
    private final By tableRows = By.xpath("//table/tbody/tr");
    private final By noDataMessage = By.xpath("//td[contains(text(), 'No data') or contains(text(), 'tidak ada data') or contains(text(), 'Tidak Ada Data')]");
    
    // Pagination
    private final By rowsPerPageSelect = By.xpath("//div[contains(@class, 'MuiSelect-select') or contains(@class, 'MuiTablePagination-select')]");
    private final By nextPageButton = By.xpath("//div[contains(@class, 'MuiTablePagination-actions')]//button[@aria-label='Go to next page' or @aria-label='next page' or @title='Go to next page']");
    private final By prevPageButton = By.xpath("//div[contains(@class, 'MuiTablePagination-actions')]//button[@aria-label='Go to previous page' or @aria-label='previous page' or @title='Go to previous page']");
    private final By paginationInfo = By.xpath("//p[contains(@class, 'MuiTablePagination-displayedRows') or contains(text(), 'of')]");

    public KoreksiPage() {
        super();
    }

    public void navigateToPage() {
        navigateTo("https://magang.dikahadir.com/laporan/koreksi");
        waitForUrlContains("laporan/koreksi");
    }

    public void waitForPagination() {
        waitForVisibility(paginationInfo);
    }

    public void enterSearchKeyword(String keyword) {
        WebElement input = findElement(searchInput);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(keyword);
    }

    public void enterStartDate(String date) {
        WebElement input = findElement(startDateInput);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(date);
    }

    public void enterEndDate(String date) {
        WebElement input = findElement(endDateInput);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(date);
    }

    public void clickSearch() {
        click(searchButton);
        try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void clickReset() {
        click(resetButton);
        try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public List<WebElement> getTableRows() {
        waitForElementPresent(By.xpath("//table"));
        return findAll(tableRows);
    }

    public String getFirstRowUserName() {
        List<WebElement> rows = getTableRows();
        if (rows.isEmpty() || isNoDataDisplayed()) {
            return null;
        }
        WebElement firstRowCell = rows.get(0).findElement(By.xpath("./td[1]"));
        return firstRowCell.getText().trim();
    }

    public boolean isNoDataDisplayed() {
        try {
            return isDisplayed(noDataMessage) || 
                   findAll(tableRows).get(0).getText().contains("No data") ||
                   findAll(tableRows).get(0).getText().contains("tidak ada");
        } catch (Exception e) {
            return false;
        }
    }

    public void selectRowsPerPage(String size) {
        click(rowsPerPageSelect);
        By optionLocator = By.xpath("//li[@role='option' and (text()='" + size + "' or @data-value='" + size + "')]");
        click(optionLocator);
        try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void clickNextPage() {
        By xpath = By.xpath("//button[@title='Go to next page' or @aria-label='Go to next page']");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(xpath));
        try {
            btn.click();
            logger.info("Clicked Next Page button successfully");
        } catch (Exception e) {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", btn);
            logger.info("Clicked Next Page button via JS execution");
        }
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void clickPrevPage() {
        By xpath = By.xpath("//button[@title='Go to previous page' or @aria-label='Go to previous page']");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(xpath));
        try {
            btn.click();
            logger.info("Clicked Previous Page button successfully");
        } catch (Exception e) {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", btn);
            logger.info("Clicked Previous Page button via JS execution");
        }
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public String getPaginationText() {
        return getText(paginationInfo).trim();
    }
}
