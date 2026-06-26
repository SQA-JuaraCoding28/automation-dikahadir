# Dikahadir Automation

---

## Tech Stack

| Technology      | Version         | Purpose                                 |
|----------------|-----------------|------------------------------------------|
| Java            | 21              | Programming language                    |
| Maven           | 3.9+            | Build tool & dependency management      |
| Selenium        | 4.44.0          | Browser automation                      |
| Cucumber        | 7.34.3          | BDD framework & Gherkin syntax          |
| TestNG          | 7.12.0          | Test runner & assertions                |
| PicoContainer   | 7.34.3          | Dependency injection between steps      |
| WebDriverManager| 5.7.0           | Automatic driver binary management      |
| SLF4J + Logback | 2.0.18 / 1.5.34 | Logging framework                      |
| org.json        | 20231013        | JSON parsing for SIT report generation  |

---

## Project Structure

```
selenium-cucumber-boilerplate/
├── pom.xml
├── README.md
├── CONTRIBUTING.md
└── src/test/
    ├── java/com/kelompok1/cucumber/
    │   ├── core/
    │   │   ├── ConfigReader.java          # 3-tier config loader (sysprop > env > file)
    │   │   ├── DriverManager.java         # ThreadLocal WebDriver manager
    │   │   ├── Platform.java              # Enum: WEB / MOBILE platform definitions
    │   │   ├── PlatformContext.java      # InheritableThreadLocal platform holder
    │   │   └── TestData.java              # Loads values from platform-specific test-data
    │   ├── exceptions/
    │   │   ├── ConfigurationException.java
    │   │   └── PageElementException.java
    │   ├── hooks/
    │   │   └── Hooks.java                 # @Before/@After WebDriver lifecycle
    │   ├── pages/
    │   │   ├── BasePage.java              # Reusable Selenium wrappers
    │   │   ├── web/
    │   │   │   └── LoginWebPage.java      # Page Object: Web Admin Login
    │   │   └── mobile/
    │   │       └── LoginMobilePage.java   # Page Object: Mobile Mirroring Login
    │   ├── reporting/
    │   │   └── MDReportGenerator.java     # Generates SIT Markdown report from JSON
    │   ├── runners/
    │   │   ├── WebTestRunner.java         # TestNG entry point for Web tests
    │   │   └── MobileTestRunner.java      # TestNG entry point for Mobile tests
    │   └── stepdefinitions/
    │       ├── web/
    │       │   └── WebLoginSteps.java     # Gherkin step implementations: Web
    │       └── mobile/
    │           └── MobileLoginSteps.java  # Gherkin step implementations: Mobile
    └── resources/
        ├── config-web.properties          # Runtime config: Web Admin Panel
        ├── config-mobile.properties       # Runtime config: Mobile Mirroring Panel
        ├── cucumber.properties            # cucumber.publish.enabled only
        ├── logback.xml                    # Logging with rolling file appender
        ├── test-data-web.properties       # Test data: Web credentials & messages
        ├── test-data-mobile.properties    # Test data: Mobile credentials & messages
        └── features/
            ├── web/
            │   └── login.feature          # Gherkin: Web Login scenarios
            └── mobile/
                └── login.feature          # Gherkin: Mobile Login scenarios
```

---

## Prerequisites

- **Java JDK** 21 or higher
- **Maven** 3.9 or higher
- **Chrome**, **Firefox**, or **Edge** installed

```bash
java -version
mvn -version
```

---

## Quick Start

### 1. Clone

```bash
git clone <repository-url>
cd selenium-cucumber-boilerplate
```

### 2. Run Web tests only

```bash
mvn clean test -Dtest=WebTestRunner
```

### 3. Run Mobile tests only

```bash
mvn clean test -Dtest=MobileTestRunner
```

### 4. Run all tests (both runners)

```bash
mvn clean test
```

### 5. Run with a specific browser

```bash
mvn clean test -Dtest=WebTestRunner -Dbrowser=firefox
mvn clean test -Dtest=WebTestRunner -Dbrowser=edge
```

### 6. Run in headed mode (visible browser)

```bash
mvn clean test -Dtest=WebTestRunner -Dheadless=false
```

### 7. Run by tag

```bash
mvn clean test -Dtest=WebTestRunner -Dcucumber.filter.tags="@smoke"
mvn clean test -Dtest=WebTestRunner -Dcucumber.filter.tags="@positive"
mvn clean test -Dtest=WebTestRunner -Dcucumber.filter.tags="@negative"
```

---

## Configuration

### Platform-specific config files

| File | Platform | Purpose |
|------|----------|---------|
| `config-web.properties` | Web | `base.url`, `dashboard.url`, browser, timeouts |
| `config-mobile.properties` | Mobile | `base.url`, `dashboard.url`, browser, timeouts |

Controls runtime behavior — safe to commit.

```properties
# Example from config-web.properties
base.url=https://magang.dikahadir.com/authentication/login
dashboard.url=https://magang.dikahadir.com/dashboards/pending
browser=chrome
headless=true
standard.wait=15
```

### Override priority

Values are resolved in this order (first match wins):

```
1. JVM system property   → -Dbase.url=https://staging.example.com
2. Environment variable  → BASE_URL=https://staging.example.com
3. Platform config file  → fallback default
```

This means CI pipelines can inject any value without touching source files:

```bash
# GitHub Actions / Jenkins
BASE_URL=https://staging.dikahadir.com BROWSER=firefox mvn clean test
```

---

## Test Data

All test data lives exclusively in platform-specific `.properties` files. The `TestData` class loads from the active platform's file — there are no duplicate constants in Java.

| File | Contents |
|------|----------|
| `test-data-web.properties` | Web admin credentials, error messages |
| `test-data-mobile.properties` | Mobile user credentials, error messages |

Change a value in one place only.

---

## Reporting

| Report | Location | Description |
|--------|----------|-------------|
| HTML | `target/cucumber-reports/{web,mobile}/cucumber.html` | Cucumber native HTML report |
| JSON | `target/cucumber-reports/{web,mobile}/cucumber.json` | Cucumber native JSON (source for SIT MD) |
| JUnit XML | `target/cucumber-reports/{web,mobile}/cucumber.xml` | CI integration |
| Timeline | `target/cucumber-reports/{web,mobile}/timeline/` | Parallel execution timeline |
| **SIT MD** | `target/sit-reports/SIT_Report_{PLATFORM}_YYYY-MM-DD_HHMMSS.md` | **Markdown SIT document for Excel conversion** |
| Log file | `target/logs/test.log` | Rolling application logs |

### SIT Markdown Report

After each test run, `MDReportGenerator` automatically produces a Markdown table suitable for conversion to Excel. The table contains:

| Column | Source |
|--------|--------|
| Test Case ID | Auto-generated (`TC-001`, `TC-002`...) |
| Module | Feature name |
| Test Scenario | Scenario name from `.feature` |
| Type | Parsed from tags (`@positive`, `@negative`, `@happy-path`, `@edge-case`) |
| Platform | Parsed from tags (`@web`, `@mobile`) or runner fallback |
| Preconditions | `Background` steps concatenated |
| Test Data | Quoted strings from `Given`/`When` steps |
| Test Step | All steps before `Then` |
| Expected Result | `Then` steps and following `And`/`But` |
| Actual Result | `As expected` if passed; error message if failed |
| Status | `PASSED` / `FAILED` / `SKIPPED` |
| Note | Empty if passed; error message if failed |
| Evidence | `Screenshot attached` if failed |

```bash
# Open HTML report
open target/cucumber-reports/web/cucumber.html        # macOS
xdg-open target/cucumber-reports/web/cucumber.html   # Linux
start target/cucumber-reports/web/cucumber.html       # Windows
```

---

## Extending the Boilerplate

### Adding a new feature to Web

1. **Page Object** → `src/test/java/.../pages/web/InventoryWebPage.java`
2. **Step Definitions** → `src/test/java/.../stepdefinitions/web/InventoryWebSteps.java`
3. **Feature file** → `src/test/resources/features/web/inventory.feature`
4. **Test data** → add keys to `test-data-web.properties`

### Adding a new feature to Mobile

1. **Page Object** → `src/test/java/.../pages/mobile/InventoryMobilePage.java`
2. **Step Definitions** → `src/test/java/.../stepdefinitions/mobile/InventoryMobileSteps.java`
3. **Feature file** → `src/test/resources/features/mobile/inventory.feature`
4. **Test data** → add keys to `test-data-mobile.properties`

### Adding a new runner

If you add a third platform, create a new runner that:
- Sets `PlatformContext.set(Platform.YOUR_PLATFORM)` in a static initializer
- Points `features` and `glue` to the correct directories
- Calls `MDReportGenerator.generate("your_platform")` in `tearDownClass()`

---

## Design Decisions

| Decision | Rationale |
|---|---|
| Dual platform architecture (WEB / MOBILE) | Two distinct UIs with different URLs, credentials, and locators |
| `Platform` enum + `PlatformContext` | Clean separation of config, test data, and page objects per platform |
| `InheritableThreadLocal` for platform | Child threads from parallel `DataProvider` inherit the platform automatically |
| Separate runners per platform | Each runner loads only its own features, steps, and config |
| `return this` in page methods | Fluent API — steps read like sentences |
| `ThreadLocal` WebDriver | Safe parallel execution — no shared state |
| Lazy page object creation | Prevents NPE from BasePage reading driver before `@Before` fires |
| 3-tier config loading | CI can inject values without touching files |
| Single source of truth for test data | Prevents `test-data.properties` vs `TestData.java` drift |
| No raw passwords in `.feature` files | Gherkin is shared widely; secrets don't belong there |
| SIT MD report from JSON | Reuses Cucumber's native JSON output; no extra plugin complexity |
| `append=false` + rolling log | Each run gets a clean log; history is archived, not accumulated |
| `not @wip` in runner | In-progress scenarios excluded from CI automatically |

---

## Troubleshooting

### Driver version mismatch
WebDriverManager handles this automatically. If issues persist:
```bash
mvn clean test -Dwdm.clearDriverCache=true
```

### Tests pass locally but fail in CI headless
Increase timeout in config or override via env var:
```bash
STANDARD_WAIT=25 mvn clean test
```

### NPE in BasePage constructor
Page objects must be created inside step methods, not in the step definition constructor. See `WebLoginSteps.loginPage()` for the correct lazy initialization pattern.

### SIT MD report is empty / not generated
Ensure the runner's `tearDownClass()` calls `super.tearDownClass()` **before** `MDReportGenerator.generate()`. Cucumber's JSON file is only flushed to disk after `super.tearDownClass()` completes.

---

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for Git workflow, branch naming, and commit message conventions.
