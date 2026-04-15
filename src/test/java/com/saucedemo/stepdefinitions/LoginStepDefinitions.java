package com.saucedemo.stepdefinitions;

import com.saucedemo.core.EnvironmentConfig;
import com.saucedemo.models.UserCredentials;
import com.saucedemo.questions.CurrentPageTitle;
import com.saucedemo.models.UserRole;
import com.saucedemo.questions.CurrentPath;
import com.saucedemo.questions.IsOnInventoryPage;
import com.saucedemo.questions.LoginErrorMessage;
import com.saucedemo.tasks.LoginWithCredentials;
import com.saucedemo.tasks.OpenTheLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class LoginStepDefinitions {

    @Given("user opens the login page")
    public void userOpensTheLoginPage() {
        OnStage.theActorInTheSpotlight().attemptsTo(OpenTheLoginPage.now());
    }

    @When("user logs in as {string}")
    public void userLogsInAs(String role) {
        loginWithRole(role);
    }

    @When("user attempts to login as {string}")
    public void userAttemptsToLoginAs(String role) {
        loginWithRole(role);
    }

    @Then("user should be redirected to the inventory page")
    public void userShouldBeRedirectedToInventoryPage() {
        String expectedPath = EnvironmentConfig.required("expected.path");
        String expectedTitle = EnvironmentConfig.required("expected.title");
        OnStage.theActorInTheSpotlight().should(
                seeThat(CurrentPath.value(), equalTo(expectedPath)),
                seeThat(CurrentPageTitle.value(), equalTo(expectedTitle)),
                seeThat(IsOnInventoryPage.displayed(), is(true))
        );
    }

    @Then("user should see the error {string}")
    public void userShouldSeeTheError(String expectedError) {
        OnStage.theActorInTheSpotlight().should(
                seeThat(LoginErrorMessage.value(), containsString(expectedError))
        );
    }

    private void loginWithRole(String role) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                LoginWithCredentials.using(UserCredentials.forRole(UserRole.from(role)))
        );
    }
}
