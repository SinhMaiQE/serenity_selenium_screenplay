package com.saucedemo.core;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;

public final class UiWaits {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    private UiWaits() {
    }

    public static Performable untilVisible(Target target) {
        return WaitUntil.the(target, WebElementStateMatchers.isVisible())
                .forNoMoreThan(resolveTimeout()).seconds();
    }

    public static Performable untilClickable(Target target) {
        return WaitUntil.the(target, WebElementStateMatchers.isClickable())
                .forNoMoreThan(resolveTimeout()).seconds();
    }

    private static int resolveTimeout() {
        return EnvironmentConfig.getInt("timeout", DEFAULT_TIMEOUT_SECONDS);
    }
}
