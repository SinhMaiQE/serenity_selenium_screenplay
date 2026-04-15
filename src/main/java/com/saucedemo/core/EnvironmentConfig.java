package com.saucedemo.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class EnvironmentConfig {

    private static final String DEFAULT_ENV = "dev";
    private static final Properties PROPERTIES = loadProperties(resolveEnv());

    private EnvironmentConfig() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String required(String key) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required config key: " + key);
        }
        return value;
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        int parsedValue;
        try {
            parsedValue = Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid integer value for config key '" + key + "': " + value, e);
        }
        if (parsedValue <= 0) {
            throw new IllegalStateException("Config key '" + key + "' must be greater than 0, but was: " + parsedValue);
        }
        return parsedValue;
    }

    private static String resolveEnv() {
        String envFromSystem = System.getProperty("env");
        if (envFromSystem == null || envFromSystem.isBlank()) {
            return DEFAULT_ENV;
        }
        return envFromSystem.trim().toLowerCase();
    }

    private static Properties loadProperties(String env) {
        String resourcePath = String.format("config/%s.properties", env);
        try (InputStream inputStream = EnvironmentConfig.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (Objects.isNull(inputStream)) {
                throw new IllegalStateException("Config file not found: " + resourcePath);
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config file: " + resourcePath, e);
        }
    }
}
