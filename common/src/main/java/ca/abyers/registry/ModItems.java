package ca.abyers.registry;

import ca.abyers.ExampleMod;
import ca.abyers.content.light.bulb.LvLedLightBulb;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public final class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ExampleMod.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> LV_LED_BULB = ITEMS.register("lv_led_bulb", () -> new LvLedLightBulb(new Item.Properties()));

    private ModItems() {
    }

    public static void register() {
        ITEMS.register();
    }
}
