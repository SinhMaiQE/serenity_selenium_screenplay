package com.saucedemo.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * Loads environment-specific configuration from {@code config/<env>.properties}
 * on the classpath. The environment is selected via the {@code -Denv=...}
 * system property and defaults to {@value #DEFAULT_ENV}.
 */
public final class EnvironmentConfig {

    private static final String ENV_SYSTEM_PROPERTY = "env";
    private static final String DEFAULT_ENV = "dev";
    private static final String CONFIG_RESOURCE_PATTERN = "config/%s.properties";

    private static final Properties PROPERTIES = loadProperties(resolveEnv());

    private EnvironmentConfig() {
    }

    /** Returns the raw value for {@code key}, or {@code null} if not set. */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    /** Returns the value for {@code key}, throwing if missing or blank. */
    public static String required(String key) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required config key: " + key);
        }
        return value;
    }

    /** Returns the int value for {@code key}, falling back to {@code defaultValue} when missing or blank. */
    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "Invalid integer value for config key '" + key + "': " + value, e);
        }
    }

    private static String resolveEnv() {
        String env = System.getProperty(ENV_SYSTEM_PROPERTY);
        return (env == null || env.isBlank())
                ? DEFAULT_ENV
                : env.trim().toLowerCase(Locale.ROOT);
    }

    private static Properties loadProperties(String env) {
        String resourcePath = String.format(CONFIG_RESOURCE_PATTERN, env);
        ClassLoader classLoader = EnvironmentConfig.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Config file not found on classpath: " + resourcePath);
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config file: " + resourcePath, e);
        }
    }
}
