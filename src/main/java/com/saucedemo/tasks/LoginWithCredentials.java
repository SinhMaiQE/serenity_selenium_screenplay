package com.saucedemo.tasks;

import com.saucedemo.core.BaseTask;
import com.saucedemo.core.UiWaits;
import com.saucedemo.models.UserCredentials;
import com.saucedemo.ui.LoginPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

public class LoginWithCredentials extends BaseTask {

    private final String username;
    private final String password;

    public LoginWithCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Performable using(UserCredentials credentials) {
        return instrumented(
                LoginWithCredentials.class,
                credentials.getUsername(),
                credentials.getPassword()
        );
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                UiWaits.untilVisible(LoginPage.USERNAME_INPUT),
                Enter.theValue(username).into(LoginPage.USERNAME_INPUT),
                UiWaits.untilVisible(LoginPage.PASSWORD_INPUT),
                Enter.theValue(password).into(LoginPage.PASSWORD_INPUT),
                UiWaits.untilClickable(LoginPage.LOGIN_BUTTON),
                Click.on(LoginPage.LOGIN_BUTTON)
        );
    }
}
