package com.saucedemo.ui;

import net.serenitybdd.screenplay.targets.Target;

public class LoginPage {

    private LoginPage() {
    }

    public static final Target USERNAME_INPUT = Target.the("username input")
            .locatedBy("#user-name");

    public static final Target PASSWORD_INPUT = Target.the("password input")
            .locatedBy("#password");

    public static final Target LOGIN_BUTTON = Target.the("login button")
            .locatedBy("#login-button");

    public static final Target ERROR_MESSAGE = Target.the("error message")
            .locatedBy("[data-test='error']");
}
