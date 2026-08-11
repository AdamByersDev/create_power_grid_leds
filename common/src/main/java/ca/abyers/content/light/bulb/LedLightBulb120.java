package ca.abyers.content.light.bulb;

import net.minecraft.world.item.Item;

public class LedLightBulb120 extends BaseLedLightBulb {
    private static final float RATED_VOLTAGE_VOLTS = 120.0f;
    private static final String MODEL_SUFFIX = "120";

    public LedLightBulb120(Item.Properties settings) {
        super(settings, RATED_VOLTAGE_VOLTS, MODEL_SUFFIX);
    }
}
