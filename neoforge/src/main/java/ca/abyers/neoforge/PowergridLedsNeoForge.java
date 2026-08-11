package ca.abyers.neoforge;

import net.neoforged.fml.common.Mod;

import ca.abyers.PowergridLeds;

@Mod(PowergridLeds.MOD_ID)
public final class PowergridLedsNeoForge {
    public PowergridLedsNeoForge() {
        PowergridLeds.init();
    }
}
