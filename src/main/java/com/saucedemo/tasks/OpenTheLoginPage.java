package com.saucedemo.tasks;

import com.saucedemo.core.BaseTask;
import com.saucedemo.core.EnvironmentConfig;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Open;

/**
 * Navigates the actor's browser to the application's {@code base.url}.
 */
public class OpenTheLoginPage extends BaseTask {

    private static final String BASE_URL_KEY = "base.url";

    public static Performable now() {
        return instrumented(OpenTheLoginPage.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.url(EnvironmentConfig.required(BASE_URL_KEY)));
    }
}
