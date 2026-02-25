package ca.abyers.content.light.bulb;

import ca.abyers.PowergridLeds;
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
    private static final float RATED_POWER_WATTS = 3.0f;
    private static final float RATED_VOLTAGE_VOLTS = 12.0f;
    private static final float TEMPERATURE_AT_RATED_RESISTANCE = 1450.0f;
    private static final float MIN_RESISTANCE_FACTOR = 0.85f;
    private static final float THERMAL_MASS = 0.00015f;
    private static final float OVERHEAT_TEMPERATURE = 2100.0f;
    private static final float DISSIPATION_DIVISOR = 1450.0f;
    private static final PartialModel MODEL_OFF = partial("block/lamps/lv_light_bulb");
    private static final PartialModel MODEL_ON = partial("block/lamps/lv_light_bulb_on");
    private static final PartialModel MODEL_BROKEN = partial("block/lamps/lv_light_bulb_broken");
    private static final PartialModel MODEL_LIGHT = partial("block/lamps/lv_light_bulb_light");
    private static final PartialModel DYED_MODEL_OFF = partial("block/lamps/lv_dyed_light_bulb");
    private static final PartialModel DYED_MODEL_ON = partial("block/lamps/lv_dyed_light_bulb_on");
    private static final PartialModel DYED_MODEL_BROKEN = partial("block/lamps/lv_dyed_light_bulb_broken");
    private static final PartialModel DYED_MODEL_LIGHT = partial("block/lamps/lv_dyed_light_bulb_light");
    private static final PartialModel DYED_MODEL_BULB = partial("block/lamps/lv_dyed_light_bulb_bulb");

    public LvLedLightBulb(Item.Properties settings) {
        super(settings);
        this.canBeDyed = true;
        this.modelSupplier = () -> state -> switch (state) {
            case OFF -> MODEL_OFF;
            case LOW_POWER, ON -> MODEL_ON;
            case BROKEN -> MODEL_BROKEN;
            case LIGHT -> MODEL_LIGHT;
        };
        this.dyedModelSupplier = () -> state -> switch (state) {
            case OFF -> DYED_MODEL_OFF;
            case LOW_POWER, ON -> DYED_MODEL_ON;
            case BROKEN -> DYED_MODEL_BROKEN;
            case LIGHT -> DYED_MODEL_LIGHT;
            case BULB -> DYED_MODEL_BULB;
        };
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

    private static PartialModel partial(String path) {
        return PartialModel.of(powerGridId(path));
    }

    private static ResourceLocation powerGridId(String path) {
        return new ResourceLocation(PowergridLeds.MOD_ID, path);
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
