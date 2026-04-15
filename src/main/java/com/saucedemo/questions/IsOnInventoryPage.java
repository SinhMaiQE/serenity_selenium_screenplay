package com.saucedemo.questions;

import com.saucedemo.core.BaseQuestion;
import com.saucedemo.ui.InventoryPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class IsOnInventoryPage extends BaseQuestion<Boolean> {

    public static Question<Boolean> displayed() {
        return new IsOnInventoryPage();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        return InventoryPage.INVENTORY_LIST.resolveFor(actor).isVisible();
    }
}
