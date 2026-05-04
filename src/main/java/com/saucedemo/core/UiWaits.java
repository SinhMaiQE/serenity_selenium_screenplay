package com.saucedemo.core;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;

/**
 * Reusable {@link Performable} wait helpers. Timeout is sourced from the
 * {@code timeout} config key and falls back to {@link #DEFAULT_TIMEOUT_SECONDS}.
 */
public final class UiWaits {

    private static final String TIMEOUT_CONFIG_KEY = "timeout";
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    private UiWaits() {
    }

    public static Performable untilVisible(Target target) {
        return WaitUntil.the(target, WebElementStateMatchers.isVisible())
                .forNoMoreThan(timeoutSeconds()).seconds();
    }

    public static Performable untilClickable(Target target) {
        return WaitUntil.the(target, WebElementStateMatchers.isClickable())
                .forNoMoreThan(timeoutSeconds()).seconds();
    }

    private static int timeoutSeconds() {
        return EnvironmentConfig.getInt(TIMEOUT_CONFIG_KEY, DEFAULT_TIMEOUT_SECONDS);
    }
}
