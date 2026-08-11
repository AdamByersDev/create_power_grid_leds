package ca.abyers.neoforge;

import dev.architectury.platform.neoforge.EventBuses;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

import ca.abyers.PowergridLeds;

@Mod(PowergridLeds.MOD_ID)
public final class PowergridLedsNeoForge {
    public PowergridLedsNeoForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(PowergridLeds.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        PowergridLeds.init();
    }
}
