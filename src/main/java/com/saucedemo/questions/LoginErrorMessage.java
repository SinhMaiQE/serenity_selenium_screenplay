package com.saucedemo.questions;

import com.saucedemo.core.BaseQuestion;
import com.saucedemo.ui.LoginPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

/** Returns the visible login error message text, or empty string if none. */
public class LoginErrorMessage extends BaseQuestion<String> {

    public static Question<String> value() {
        return new LoginErrorMessage();
    }

    @Override
    public String answeredBy(Actor actor) {
        return Text.of(LoginPage.ERROR_MESSAGE).answeredBy(actor);
    }
}
