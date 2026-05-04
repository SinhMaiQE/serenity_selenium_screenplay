package com.saucedemo.models;

/**
 * Immutable username/password pair used by login tasks.
 *
 * <p>Use {@link #forRole(UserRole)} to load credentials for a known role from
 * environment variables / config files.
 */
public record UserCredentials(String username, String password) {

    public static UserCredentials forRole(UserRole role) {
        return new UserCredentials(role.username(), role.password());
    }
}
