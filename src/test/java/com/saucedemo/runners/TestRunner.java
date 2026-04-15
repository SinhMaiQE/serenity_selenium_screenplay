
package com.saucedemo.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.saucedemo.hooks", "com.saucedemo.stepdefinitions"},
        plugin = {"pretty"}
)
public class TestRunner {}
