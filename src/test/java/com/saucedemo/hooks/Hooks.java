package com.saucedemo.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import org.openqa.selenium.WebDriver;

import java.util.Locale;
import java.util.Optional;

/**
 * Cucumber lifecycle hooks: prepares the Screenplay stage and assigns the
 * spotlight actor before each scenario; tears the stage down afterwards.
 *
 * <p>An actor name can be set per-scenario via the {@code @actor:Name} tag.
 */
public class Hooks {

    private static final String DEFAULT_ACTOR = "User";
    private static final String ACTOR_TAG_PREFIX = "@actor:";

    @Managed
    WebDriver driver;

    @Before
    public void setUp(Scenario scenario) {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled(resolveActorName(scenario)).can(BrowseTheWeb.with(driver));
    }

    @After
    public void tearDown() {
        OnStage.drawTheCurtain();
    }

    private static String resolveActorName(Scenario scenario) {
        Optional<String> taggedActor = scenario.getSourceTagNames().stream()
                .filter(tag -> tag.toLowerCase(Locale.ROOT).startsWith(ACTOR_TAG_PREFIX))
                .map(tag -> tag.substring(ACTOR_TAG_PREFIX.length()).trim())
                .filter(name -> !name.isBlank())
                .findFirst();
        return taggedActor.orElse(DEFAULT_ACTOR);
    }
}
