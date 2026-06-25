package com.kelompok1.cucumber.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestNG Cucumber Runner — entry point for mvn clean test.
 *
 * Plugin list is the single source of truth for report config.
 * cucumber.properties only holds cucumber.publish.enabled=false.
 *
 * Tags: "not @wip" excludes in-progress scenarios from CI.
 * Override at runtime: mvn clean test -Dcucumber.filter.tags="@smoke"
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {
        "com.kelompok1.cucumber.stepdefinitions",
        "com.kelompok1.cucumber.hooks"
    },
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber.html",
        "json:target/cucumber-reports/cucumber.json",
        "junit:target/cucumber-reports/cucumber.xml",
        "timeline:target/cucumber-reports/timeline"
    },
    monochrome = true,
    tags = "not @wip"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
