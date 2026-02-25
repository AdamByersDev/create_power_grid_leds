package ca.abyers;

import ca.abyers.registry.ModItems;
import ca.abyers.registry.ModCreativeTabs;

public final class PowergridLeds {
    public static final String MOD_ID = "create_power_grid_leds";

    public static void init() {
        ModItems.register();
        ModCreativeTabs.register();
    }
}
