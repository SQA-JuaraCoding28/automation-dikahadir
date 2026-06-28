package com.hadir.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class IzinPulangCepatPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By searchInput = By.id("search");
    private By startDateInput = By.xpath("//input[@placeholder='Start Date']");
    private By endDateInput = By.xpath("//input[@placeholder='End Date']");
    private By searchButton = By.xpath("//button[text()='Search' or contains(text(), 'Search')]");
    private By resetButton = By.xpath("//button[text()='Reset' or contains(text(), 'Reset')]");
    
    // Table
    private By tableRows = By.xpath("//table/tbody/tr");
    private By noDataMessage = By.xpath("//td[contains(text(), 'No data') or contains(text(), 'tidak ada data') or contains(text(), 'Tidak Ada Data')]");
    
    // Pagination
    private By rowsPerPageSelect = By.xpath("//div[contains(@class, 'MuiSelect-select') or contains(@class, 'MuiTablePagination-select')]");
    private By nextPageButton = By.xpath("//div[contains(@class, 'MuiTablePagination-actions')]//button[@aria-label='Go to next page' or @aria-label='next page' or @title='Go to next page']");
    private By prevPageButton = By.xpath("//div[contains(@class, 'MuiTablePagination-actions')]//button[@aria-label='Go to previous page' or @aria-label='previous page' or @title='Go to previous page']");
    private By paginationInfo = By.xpath("//p[contains(@class, 'MuiTablePagination-displayedRows') or contains(text(), 'of')]");

    public IzinPulangCepatPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToPage() {
        driver.get("https://magang.dikahadir.com/laporan/izin-pulang-cepat");
        wait.until(ExpectedConditions.urlContains("izin-pulang-cepat"));
    }

    public void enterSearchKeyword(String keyword) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        // clear using key combinations in case standard clear() doesn't trigger change events
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(keyword);
    }

    public void enterStartDate(String date) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(startDateInput));
        // Some date fields in React are tricky; we clear and send keys
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(date);
    }

    public void enterEndDate(String date) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(endDateInput));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(date);
    }

    public void clickSearch() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        btn.click();
        // Wait a short time for table reload
        try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void clickReset() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(resetButton));
        btn.click();
        try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public List<WebElement> getTableRows() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));
        return driver.findElements(tableRows);
    }

    public String getFirstRowUserName() {
        List<WebElement> rows = getTableRows();
        if (rows.isEmpty() || isNoDataDisplayed()) {
            return null;
        }
        // Column 1 is Nama User (index 1 in xpath or index 0 in td list)
        WebElement firstRowCell = rows.get(0).findElement(By.xpath("./td[1]"));
        return firstRowCell.getText().trim();
    }

    public boolean isNoDataDisplayed() {
        try {
            return driver.findElements(noDataMessage).size() > 0 || 
                   driver.findElements(tableRows).get(0).getText().contains("No data") ||
                   driver.findElements(tableRows).get(0).getText().contains("tidak ada");
        } catch (Exception e) {
            return false;
        }
    }

    public void selectRowsPerPage(String size) {
        WebElement select = wait.until(ExpectedConditions.elementToBeClickable(rowsPerPageSelect));
        select.click();
        
        // Locate option in MUI dropdown menu (usually renders in listbox)
        By optionLocator = By.xpath("//li[@role='option' and (text()='" + size + "' or @data-value='" + size + "')]");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        option.click();
        try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void clickNextPage() {
        WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(nextPageButton));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn));
            btn.click();
        } catch (Exception e) {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", btn);
        }
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void clickPrevPage() {
        WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(prevPageButton));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn));
            btn.click();
        } catch (Exception e) {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", btn);
        }
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public String getPaginationText() {
        WebElement info = wait.until(ExpectedConditions.visibilityOfElementLocated(paginationInfo));
        return info.getText().trim();
    }
}
