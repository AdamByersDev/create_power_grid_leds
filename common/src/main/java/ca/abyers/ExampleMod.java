package ca.abyers;

import ca.abyers.registry.ModItems;

public final class ExampleMod {
    public static final String MOD_ID = "create_power_grid_leds";

    public static void init() {
        ModItems.register();
    }
}
