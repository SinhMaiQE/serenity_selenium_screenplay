package com.saucedemo.ui;

import net.serenitybdd.screenplay.targets.Target;

public class InventoryPage {

    private InventoryPage() {
    }

    public static final Target INVENTORY_LIST = Target.the("inventory list")
            .locatedBy(".inventory_list");

    public static final Target INVENTORY_ITEM = Target.the("inventory item")
            .locatedBy(".inventory_item");

    public static final Target SHOPPING_CART = Target.the("shopping cart")
            .locatedBy(".shopping_cart_link");
}
