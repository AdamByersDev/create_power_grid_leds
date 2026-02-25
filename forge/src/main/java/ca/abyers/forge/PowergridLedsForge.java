package ca.abyers.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import ca.abyers.PowergridLeds;

@Mod(PowergridLeds.MOD_ID)
public final class PowergridLedsForge {
    public PowergridLedsForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(PowergridLeds.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        PowergridLeds.init();
    }
}
