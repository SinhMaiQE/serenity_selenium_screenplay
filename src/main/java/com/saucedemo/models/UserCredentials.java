package com.saucedemo.models;

import com.saucedemo.core.EnvironmentConfig;

public class UserCredentials {

    private final String username;
    private final String password;

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static UserCredentials fromEnvironment() {
        String username = System.getenv("TEST_USERNAME");
        String password = System.getenv("TEST_PASSWORD");

        if (username == null || username.isBlank()) {
            username = EnvironmentConfig.required("username");
        }
        if (password == null || password.isBlank()) {
            password = EnvironmentConfig.required("password");
        }

        return new UserCredentials(
                username,
                password
        );
    }

    public static UserCredentials forRole(UserRole role) {
        return new UserCredentials(role.getUsername(), role.getPassword());
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
