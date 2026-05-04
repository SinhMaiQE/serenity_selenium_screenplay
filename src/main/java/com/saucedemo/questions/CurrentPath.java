package com.saucedemo.questions;

import com.saucedemo.core.BaseQuestion;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

import java.net.URI;

/** Returns the path component of the actor's current URL (e.g. {@code /inventory.html}). */
public class CurrentPath extends BaseQuestion<String> {

    public static Question<String> value() {
        return new CurrentPath();
    }

    @Override
    public String answeredBy(Actor actor) {
        String currentUrl = BrowseTheWeb.as(actor).getDriver().getCurrentUrl();
        return currentUrl == null ? "" : URI.create(currentUrl).getPath();
    }
}
