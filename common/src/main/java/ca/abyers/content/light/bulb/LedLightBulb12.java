package ca.abyers.content.light.bulb;

import net.minecraft.world.item.Item;

public class LedLightBulb12 extends BaseLedLightBulb {
    private static final float RATED_VOLTAGE_VOLTS = 12.0f;
    private static final String MODEL_SUFFIX = "12";

    public LedLightBulb12(Item.Properties settings) {
        super(settings, RATED_VOLTAGE_VOLTS, MODEL_SUFFIX);
    }
}
