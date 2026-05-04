package com.saucedemo.stepdefinitions;

import com.saucedemo.core.EnvironmentConfig;
import com.saucedemo.models.UserCredentials;
import com.saucedemo.models.UserRole;
import com.saucedemo.questions.CurrentPageTitle;
import com.saucedemo.questions.CurrentPath;
import com.saucedemo.questions.IsOnInventoryPage;
import com.saucedemo.questions.LoginErrorMessage;
import com.saucedemo.tasks.LoginWithCredentials;
import com.saucedemo.tasks.OpenTheLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class LoginStepDefinitions {

    private static final String EXPECTED_PATH_KEY = "expected.path";
    private static final String EXPECTED_TITLE_KEY = "expected.title";

    @Given("user opens the login page")
    public void userOpensTheLoginPage() {
        theActorInTheSpotlight().attemptsTo(OpenTheLoginPage.now());
    }

    @When("user logs in as {string}")
    public void userLogsInAs(String role) {
        loginAs(role);
    }

    @When("user attempts to login as {string}")
    public void userAttemptsToLoginAs(String role) {
        loginAs(role);
    }

    @Then("user should be redirected to the inventory page")
    public void userShouldBeRedirectedToInventoryPage() {
        theActorInTheSpotlight().should(
                seeThat(CurrentPath.value(), equalTo(EnvironmentConfig.required(EXPECTED_PATH_KEY))),
                seeThat(CurrentPageTitle.value(), equalTo(EnvironmentConfig.required(EXPECTED_TITLE_KEY))),
                seeThat(IsOnInventoryPage.displayed(), is(true))
        );
    }

    @Then("user should see the error {string}")
    public void userShouldSeeTheError(String expectedError) {
        theActorInTheSpotlight().should(
                seeThat(LoginErrorMessage.value(), containsString(expectedError))
        );
    }

    private static void loginAs(String role) {
        UserCredentials credentials = UserCredentials.forRole(UserRole.from(role));
        theActorInTheSpotlight().attemptsTo(LoginWithCredentials.using(credentials));
    }
}
