package com.saucedemo.questions;

import com.saucedemo.core.BaseQuestion;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

/** Returns the current browser tab title. */
public class CurrentPageTitle extends BaseQuestion<String> {

    public static Question<String> value() {
        return new CurrentPageTitle();
    }

    @Override
    public String answeredBy(Actor actor) {
        return BrowseTheWeb.as(actor).getDriver().getTitle();
    }
}
