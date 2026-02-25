package ca.abyers.registry;

import ca.abyers.PowergridLeds;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(PowergridLeds.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register(
            "main",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup." + PowergridLeds.MOD_ID))
                    .icon(() -> new ItemStack(ModItems.LED_BULB.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.LED_FILAMENT.get());
                        output.accept(ModItems.LED_BULB.get());
                        output.accept(ModItems.LV_LED_BULB.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register() {
        CREATIVE_MODE_TABS.register();
    }
}
