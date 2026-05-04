package com.saucedemo.models;

import com.saucedemo.core.EnvironmentConfig;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Test users supported by the framework. Credentials for each role are
 * resolved (in order) from:
 * <ol>
 *     <li>environment variable {@code TEST_<ROLE>_USERNAME} / {@code TEST_<ROLE>_PASSWORD}</li>
 *     <li>config key {@code credentials.<role>.username} / {@code credentials.<role>.password}</li>
 * </ol>
 */
public enum UserRole {

    STANDARD,
    LOCKED_OUT,
    INVALID;

    private static final String ENV_KEY_PATTERN = "TEST_%s_%s";
    private static final String CONFIG_KEY_PATTERN = "credentials.%s.%s";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    /** Parses a role name from feature files (case-insensitive, accepts spaces or dashes). */
    public static UserRole from(String value) {
        String normalized = value == null
                ? ""
                : value.trim().replace('-', '_').replace(' ', '_').toUpperCase(Locale.ROOT);
        try {
            return UserRole.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            String allowed = Arrays.stream(values()).map(Enum::name).collect(Collectors.joining(", "));
            throw new IllegalArgumentException(
                    "Invalid user role '" + value + "'. Supported roles: " + allowed, ex);
        }
    }

    public String username() {
        return resolveCredential(USERNAME);
    }

    public String password() {
        return resolveCredential(PASSWORD);
    }

    private String resolveCredential(String credentialPart) {
        String envKey = String.format(ENV_KEY_PATTERN, name(), credentialPart);
        String fromEnv = System.getenv(envKey);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        String configKey = String.format(
                CONFIG_KEY_PATTERN, name().toLowerCase(Locale.ROOT), credentialPart.toLowerCase(Locale.ROOT));
        return EnvironmentConfig.required(configKey);
    }
}
