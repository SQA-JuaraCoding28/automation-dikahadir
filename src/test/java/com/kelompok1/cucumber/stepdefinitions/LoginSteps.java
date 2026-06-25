package com.kelompok1.cucumber.stepdefinitions;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.kelompok1.cucumber.core.TestData;
import com.kelompok1.cucumber.pages.LoginPage;

/**
 * Step Definitions for Login feature — Hadir Application.
 *
 * LoginPage is created lazily via loginPage() method instead of
 * in the constructor. This prevents a NullPointerException caused by BasePage
 * trying to read DriverManager.getDriver() before Hooks.setUp() has run.
 *
 * Cucumber wires step definition classes before @Before fires, so any field
 * initialized at construction time that touches the WebDriver will get null.
 *
 * Step inventory:
 *   @Given  — navigation and preconditions
 *   @When   — user actions (3 steps covering all login scenarios)
 *   @Then   — assertions (4 steps covering success, error, browser validation, page state)
 */
public class LoginSteps {

    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);

    /**
     * Returns a fresh LoginPage instance on every call.
     * Safe because by the time any step method runs,
     * Hooks.setUp() has already placed the WebDriver in DriverManager.
     */
    private LoginPage loginPage() {
        return new LoginPage();
    }

    // =========================================================================
    // GIVEN — Navigation & Preconditions
    // =========================================================================

    /**
     * Navigates to the Hadir login page and verifies it loaded correctly.
     * Used in Background so every scenario starts from a known state.
     */
    @Given("user is on the Hadir login page")
    public void userIsOnLoginPage() {
        loginPage().navigateToLoginPage();
        Assert.assertTrue(loginPage().isLoginPageDisplayed(),
            "Login page should be displayed — check base.url in config.properties");
        Assert.assertTrue(loginPage().isLogoDisplayed(),
            "Hadir logo should be visible on the login page");
    }

    /**
     * Full login precondition for scenarios that require an authenticated session.
     * Credentials are loaded from test-data.properties — not hardcoded here.
     */
    @Given("user is logged in")
    public void userIsLoggedIn() {
        logger.info("Setting up authenticated session...");
        loginPage().navigateToLoginPage();
        loginPage().doLogin(
            TestData.get("user.valid.email"),
            TestData.get("user.valid.password")
        );
        Assert.assertTrue(loginPage().isLoginSuccessful(),
            "Precondition failed: could not log in. Check credentials in test-data.properties");
    }

    // =========================================================================
    // WHEN — User Actions
    // =========================================================================

    /**
     * Happy path login using credentials from test-data.properties.
     * Credentials never appear in the feature file — role-based step.
     *
     * Matches: "When user logs in with valid credentials"
     */
    @When("user logs in with valid credentials")
    public void userLogsInWithValidCredentials() {
        logger.info("Logging in with valid credentials from test-data.properties");
        loginPage().doLogin(
            TestData.get("user.valid.email"),
            TestData.get("user.valid.password")
        );
    }

    /**
     * Parameterized login step for negative scenarios.
     * Accepts any email/password combination including empty strings "".
     *
     * Used for:
     *   - Empty email + empty password
     *   - Valid email + wrong password
     *   - Invalid email format (non-@ cases handled server-side)
     *
     * Matches: "When user logs in with email {string} and password {string}"
     *
     * NOTE: Do NOT use this step with the real valid password inline in the
     * feature file. Use "user logs in with valid credentials" for the happy path.
     */
    @When("user logs in with email {string} and password {string}")
    public void userLogsInWithEmailAndPassword(String email, String password) {
        logger.info("Attempting login with email: '{}'", email);
        loginPage().doLogin(email, password);
    }

    /**
     * Triggers browser-native HTML5 validation without completing a server-side request.
     * Used specifically for the missing '@' scenario where the browser intercepts
     * the submit and shows a validationMessage on the input element — no MUI alert
     * is rendered in the DOM.
     *
     * Matches: "When user enters email {string} and password {string} without submitting"
     */
    @When("user enters email {string} and password {string} without submitting")
    public void userEntersEmailWithoutSubmitting(String email, String password) {
        logger.info("Entering email '{}' to trigger browser validation", email);
        loginPage().enterEmail(email)
                   .enterPassword(password)
                   .clickLogin(); // browser intercepts here — no server call made
    }

    // =========================================================================
    // THEN — Assertions
    // =========================================================================

    /**
     * Verifies the user reached the dashboard after a successful login.
     * LoginPage.isLoginSuccessful() checks the URL, not a DOM element,
     * which is more reliable during page transitions.
     */
    @Then("user should be redirected to the dashboard")
    public void userRedirectedToDashboard() {
        Assert.assertTrue(loginPage().isLoginSuccessful(),
            "Expected redirect to dashboard URL. Check dashboard.url in config.properties");
    }

    /**
     * Verifies a MUI error alert is visible in the DOM.
     * This covers server-side errors only (wrong password, account not found, etc.).
     * For browser-native HTML5 validation errors, use "browser validation should contain".
     */
    @Then("error message should be displayed")
    public void errorMessageDisplayed() {
        Assert.assertTrue(loginPage().isErrorDisplayed(),
            "Expected MUI error alert to be visible but it was not found");
    }

    /**
     * Verifies the text content of the MUI server-side error alert.
     * Does NOT handle browser-native HTML5 validation messages —
     * those have a dedicated step below to keep the routing explicit.
     *
     * Matches: "And error message should contain {string}"
     */
    @Then("error message should contain {string}")
    public void errorMessageContains(String expectedError) {
        String actualError = loginPage().getErrorMessage();
        Assert.assertTrue(actualError.contains(expectedError),
            String.format(
                "Expected server-side error to contain '%s' but got: '%s'",
                expectedError, actualError
            ));
    }

    /**
     * Verifies the browser-native HTML5 validationMessage on the email input.
     * This is separate from errorMessageContains because:
     *   - The message source is different (input.validationMessage vs DOM element text)
     *   - The browser intercepts form submission before any server call is made
     *   - The message text and language depends on the browser, not the app
     *
     * Matches: "And browser validation should contain {string}"
     */
    @Then("browser validation should contain {string}")
    public void browserValidationContains(String expected) {
        String actual = loginPage().getEmailValidationMessage();
        Assert.assertTrue(actual.contains(expected),
            String.format(
                "Expected browser validation message to contain '%s' but got: '%s'",
                expected, actual
            ));
    }

    /**
     * Verifies the user is still on the login page after a failed attempt.
     * Checks for presence of email field, password field, and login button.
     */
    @Then("user should remain on the login page")
    public void userRemainsOnLoginPage() {
        Assert.assertTrue(loginPage().isLoginPageDisplayed(),
            "User should still be on the login page after a failed login attempt");
    }
}
