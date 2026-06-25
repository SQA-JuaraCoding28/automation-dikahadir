package com.kelompok1.cucumber.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kelompok1.cucumber.exceptions.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration reader.
 *
 * Load priority (first match wins):
 *   1. JVM system property  (-Dkey=value)
 *   2. OS environment variable  (KEY=value, dots converted to underscores + uppercase)
 *   3. config.properties fallback
 *
 * This means CI pipelines can inject secrets as env vars without touching source files.
 */
public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        loadProperties();
    }

    private ConfigReader() {}

    private static void loadProperties() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.error("Configuration file not found: {}", CONFIG_FILE);
                throw new ConfigurationException("Configuration file not found: " + CONFIG_FILE);
            }
            properties.load(input);
            logger.info("Configuration loaded from {}", CONFIG_FILE);
        } catch (IOException e) {
            logger.error("Failed to load configuration file: {}", CONFIG_FILE, e);
            throw new ConfigurationException("Failed to load configuration file: " + CONFIG_FILE, e);
        }
    }

    /**
     * Resolves a property using the three-tier priority:
     * system property -> environment variable -> properties file.
     *
     * Environment variable name is derived by uppercasing the key
     * and replacing dots with underscores.
     * e.g. "base.url" -> "BASE_URL"
     */
    public static String getProperty(String key) {
        // 1. JVM system property
        String value = System.getProperty(key);
        if (value != null) {
            logger.debug("Property '{}' resolved from system property", key);
            return value;
        }

        // 2. OS environment variable
        String envKey = key.toUpperCase().replace(".", "_");
        value = System.getenv(envKey);
        if (value != null) {
            logger.debug("Property '{}' resolved from environment variable '{}'", key, envKey);
            return value;
        }

        // 3. Properties file fallback
        value = properties.getProperty(key);
        logger.debug("Property '{}' resolved from config file: {}", key, value);
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key, "false"));
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(getProperty(key, String.valueOf(defaultValue)));
    }

    public static int getInt(String key) {
        return Integer.parseInt(getProperty(key, "0"));
    }

    public static int getInt(String key, int defaultValue) {
        return Integer.parseInt(getProperty(key, String.valueOf(defaultValue)));
    }
}
