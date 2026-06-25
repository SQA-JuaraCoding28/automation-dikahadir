package com.kelompok1.cucumber.core;

/**
 * Centralized test data loader.
 *
 * All values are sourced from test-data.properties (single source of truth).
 * Use ConfigReader to load them — do not duplicate constants here.
 *
 * Why: having data in both Java constants and a properties file causes drift.
 * Someone updates one file and not the other, then a test fails mysteriously
 * six weeks later. One source, one place to change.
 *
 * Usage example:
 *   String username = TestData.get("user.valid.username");
 *   String error    = TestData.get("error.login.locked.user");
 */
public class TestData {

    private static final String TEST_DATA_FILE = "test-data.properties";
    private static final java.util.Properties props = new java.util.Properties();

    static {
        try (java.io.InputStream in =
                TestData.class.getClassLoader().getResourceAsStream(TEST_DATA_FILE)) {
            if (in == null) {
                throw new RuntimeException("test-data.properties not found on classpath");
            }
            props.load(in);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to load " + TEST_DATA_FILE, e);
        }
    }

    private TestData() {}

    /**
     * Returns the value for the given key from test-data.properties.
     * Throws if the key is missing so test failures are loud and obvious.
     */
    public static String get(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing test data key: '" + key + "' in " + TEST_DATA_FILE);
        }
        return value;
    }

    // Convenience constants — these are just named accessors, not duplicate values.
    // The actual strings live exclusively in test-data.properties.
    public static String validUsername()         { return get("user.valid.username"); }
    public static String validPassword()         { return get("user.valid.password"); }
    public static String lockedUsername()        { return get("user.locked.username"); }
    public static String problemUsername()       { return get("user.problem.username"); }
    public static String performanceUsername()   { return get("user.performance.username"); }

    public static String errorInvalidCredential() { return get("error.login.invalid.credential"); }
    public static String errorLockedUser()         { return get("error.login.locked.user"); }
    public static String errorUsernameRequired()   { return get("error.login.username.required"); }
    public static String errorPasswordRequired()   { return get("error.login.password.required"); }
}
