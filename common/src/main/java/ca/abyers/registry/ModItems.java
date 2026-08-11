package ca.abyers.registry;

import ca.abyers.PowergridLeds;
import ca.abyers.content.light.bulb.LedLightBulb12;
import ca.abyers.content.light.bulb.LedLightBulb120;
import ca.abyers.content.light.bulb.LedLightBulb240;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public final class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(PowergridLeds.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> LED_BULB_12 = ITEMS.register("led_bulb_12", () -> new LedLightBulb12(new Item.Properties()));
    public static final RegistrySupplier<Item> LED_BULB_120 = ITEMS.register("led_bulb_120", () -> new LedLightBulb120(new Item.Properties()));
    public static final RegistrySupplier<Item> LED_BULB_240 = ITEMS.register("led_bulb_240", () -> new LedLightBulb240(new Item.Properties()));
    public static final RegistrySupplier<Item> LED_FILAMENT = ITEMS.register("led_filament", () -> new Item(new Item.Properties()));

    private ModItems() {
    }

    public static void register() {
        ITEMS.register();
    }
}
