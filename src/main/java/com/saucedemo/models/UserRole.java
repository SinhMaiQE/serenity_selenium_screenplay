package com.saucedemo.models;

import com.saucedemo.core.EnvironmentConfig;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public enum UserRole {

    STANDARD("TEST_STANDARD_USERNAME", "TEST_STANDARD_PASSWORD", "credentials.standard.username", "credentials.standard.password"),
    LOCKED_OUT("TEST_LOCKED_OUT_USERNAME", "TEST_LOCKED_OUT_PASSWORD", "credentials.locked_out.username", "credentials.locked_out.password"),
    INVALID("TEST_INVALID_USERNAME", "TEST_INVALID_PASSWORD", "credentials.invalid.username", "credentials.invalid.password");

    private final String usernameEnvKey;
    private final String passwordEnvKey;
    private final String usernameConfigKey;
    private final String passwordConfigKey;

    UserRole(String usernameEnvKey, String passwordEnvKey, String usernameConfigKey, String passwordConfigKey) {
        this.usernameEnvKey = usernameEnvKey;
        this.passwordEnvKey = passwordEnvKey;
        this.usernameConfigKey = usernameConfigKey;
        this.passwordConfigKey = passwordConfigKey;
    }

    public static UserRole from(String value) {
        String normalized = value == null
                ? ""
                : value.trim().replace('-', '_').replace(' ', '_').toUpperCase(Locale.ROOT);
        try {
            return UserRole.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            String allowedValues = Arrays.stream(UserRole.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Invalid user role '" + value + "'. Supported roles: " + allowedValues, ex);
        }
    }

    public String getUsername() {
        return resolveCredential(usernameEnvKey, usernameConfigKey);
    }

    public String getPassword() {
        return resolveCredential(passwordEnvKey, passwordConfigKey);
    }

    private static String resolveCredential(String envKey, String configKey) {
        String fromEnvironment = System.getenv(envKey);
        if (fromEnvironment != null && !fromEnvironment.isBlank()) {
            return fromEnvironment;
        }
        return EnvironmentConfig.required(configKey);
    }
}
