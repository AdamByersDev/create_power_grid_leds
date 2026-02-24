package ca.abyers.content.light.bulb;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.patryk3211.powergrid.electricity.light.bulb.ILightBulb;
import org.patryk3211.powergrid.electricity.light.bulb.LightBulb;
import org.patryk3211.powergrid.electricity.light.bulb.LightBulbState;
import org.patryk3211.powergrid.electricity.light.fixture.LightFixtureBlockEntity;

import org.jetbrains.annotations.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.List;

public class LvLedLightBulb extends LightBulb {
    private static final float RATED_POWER_WATTS = 1.0f;
    private static final float RATED_VOLTAGE_VOLTS = 12.0f;
    private static final float TEMPERATURE_AT_RATED_RESISTANCE = 1450.0f;
    private static final float MIN_RESISTANCE_FACTOR = 0.85f;
    private static final float THERMAL_MASS = 0.003f;
    private static final float OVERHEAT_TEMPERATURE = 2100.0f;
    private static final float DISSIPATION_DIVISOR = 1450.0f;

    public LvLedLightBulb(Item.Properties settings) {
        super(settings);
        this.canBeDyed = true;
        this.modelSupplier = () -> state -> PartialModel.of(powerGridId(switch (state) {
            case OFF -> "block/lamps/light_bulb";
            case LOW_POWER, ON -> "block/lamps/light_bulb_on";
            case BROKEN -> "block/lamps/light_bulb_broken";
            case LIGHT -> "block/lamps/light_bulb_light";
        }));
        this.dyedModelSupplier = () -> state -> PartialModel.of(powerGridId(switch (state) {
            case OFF -> "block/lamps/dyed_light_bulb";
            case LOW_POWER, ON -> "block/lamps/dyed_light_bulb_on";
            case BROKEN -> "block/lamps/dyed_light_bulb_broken";
            case LIGHT -> "block/lamps/dyed_light_bulb_light";
            case BULB -> "block/lamps/dyed_light_bulb_bulb";
        }));
        applyRatedValues(
                RATED_POWER_WATTS,
                RATED_VOLTAGE_VOLTS,
                TEMPERATURE_AT_RATED_RESISTANCE,
                THERMAL_MASS
        );
    }

    @Override
    public LightBulbState createState(LightFixtureBlockEntity fixture) {
        return new State(this, fixture, modelSupplier, dyedModelSupplier);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        final boolean holdingShift = Screen.hasShiftDown();
        if (holdingShift) {
            appendProperties(stack, Minecraft.getInstance().player, tooltipComponents);
        } else {
            tooltipComponents.add(propertiesHeader(false));
        }
    }

    private static Component propertiesHeader(boolean holdingShift) {
        final String[] headerParts = Component.translatable("powergrid.tooltip.holdForDescription", "$")
                .getString()
                .split("\\$");
        final MutableComponent keyComponent = Component.translatable("create.tooltip.keyShift").copy()
                .withStyle(holdingShift ? ChatFormatting.WHITE : ChatFormatting.GRAY);
        return Component.empty()
                .append(Component.literal(headerParts[0]).withStyle(ChatFormatting.DARK_GRAY))
                .append(keyComponent)
                .append(Component.literal(headerParts[1]).withStyle(ChatFormatting.DARK_GRAY));
    }

    public static class State extends LightBulb.SimpleState {
        public <T extends Item & ILightBulb> State(
                T bulb,
                LightFixtureBlockEntity fixture,
                Supplier<Function<LightBulb.State, PartialModel>> modelProviderSupplier,
                Supplier<Function<DyedState, PartialModel>> dyedModelProviderSupplier
        ) {
            super(bulb, fixture, modelProviderSupplier, dyedModelProviderSupplier);
        }

        @Override
        protected void updatePowerLevel(int newLevel) {
            super.updatePowerLevel(newLevel > 0 ? 2 : 0);
        }
    }

    private static ResourceLocation powerGridId(String path) {
        return new ResourceLocation("powergrid", path);
    }

    private void applyRatedValues(
            float ratedPower,
            float ratedVoltage,
            float maxTemperature,
            float thermalMass
    ) {
        this.power = ratedPower;
        this.voltage = ratedVoltage;
        this.T_max = maxTemperature;
        this.R_max = (ratedVoltage * ratedVoltage) / ratedPower;
        this.R_min = this.R_max * MIN_RESISTANCE_FACTOR;
        this.thermalProperties = new ILightBulb.Properties(
                ratedPower / DISSIPATION_DIVISOR,
                thermalMass,
                OVERHEAT_TEMPERATURE
        );
    }
}
