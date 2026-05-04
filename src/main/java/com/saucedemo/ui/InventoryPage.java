package com.saucedemo.ui;

import net.serenitybdd.screenplay.targets.Target;

/** Locators for the SauceDemo inventory page. */
public final class InventoryPage {

    public static final Target INVENTORY_LIST = Target.the("inventory list")
            .locatedBy(".inventory_list");

    public static final Target INVENTORY_ITEM = Target.the("inventory item")
            .locatedBy(".inventory_item");

    public static final Target SHOPPING_CART = Target.the("shopping cart")
            .locatedBy(".shopping_cart_link");

    private InventoryPage() {
    }
}
