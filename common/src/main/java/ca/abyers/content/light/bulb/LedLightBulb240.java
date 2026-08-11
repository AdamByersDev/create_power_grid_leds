package ca.abyers.content.light.bulb;

import net.minecraft.world.item.Item;

public class LedLightBulb240 extends BaseLedLightBulb {
    private static final float RATED_VOLTAGE_VOLTS = 240.0f;
    private static final String MODEL_SUFFIX = "240";

    public LedLightBulb240(Item.Properties settings) {
        super(settings, RATED_VOLTAGE_VOLTS, MODEL_SUFFIX);
    }
}
