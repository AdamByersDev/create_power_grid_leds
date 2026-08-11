package ca.abyers.content.light.bulb;

import ca.abyers.PowergridLeds;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.patryk3211.powergrid.electricity.light.bulb.ILightBulb;
import org.patryk3211.powergrid.electricity.light.bulb.LightBulb;
import org.patryk3211.powergrid.electricity.light.bulb.LightBulbState;
import org.patryk3211.powergrid.electricity.light.fixture.LightFixtureBlockEntity;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

abstract class BaseLedLightBulb extends LightBulb {
    private static final float RATED_POWER_WATTS = 3.0f;
    private static final float TEMPERATURE_AT_RATED_RESISTANCE = 1450.0f;
    private static final float MIN_RESISTANCE_FACTOR = 0.85f;
    private static final float THERMAL_MASS = 0.00015f;
    private static final float OVERHEAT_TEMPERATURE = 2100.0f;
    private static final float DISSIPATION_DIVISOR = 1450.0f;

    protected BaseLedLightBulb(Item.Properties settings, float ratedVoltageVolts, String modelSuffix) {
        super(settings);
        final PartialModel modelOff = partial("block/lamps/light_bulb_" + modelSuffix);
        final PartialModel modelOn = partial("block/lamps/light_bulb_on_" + modelSuffix);
        final PartialModel modelBroken = partial("block/lamps/light_bulb_broken_" + modelSuffix);
        final PartialModel modelLight = partial("block/lamps/light_bulb_light_" + modelSuffix);
        final PartialModel dyedModelOff = partial("block/lamps/dyed_light_bulb_" + modelSuffix);
        final PartialModel dyedModelOn = partial("block/lamps/dyed_light_bulb_on_" + modelSuffix);
        final PartialModel dyedModelBroken = partial("block/lamps/dyed_light_bulb_broken_" + modelSuffix);
        final PartialModel dyedModelLight = partial("block/lamps/dyed_light_bulb_light_" + modelSuffix);
        final PartialModel dyedModelBulb = partial("block/lamps/dyed_light_bulb_bulb_" + modelSuffix);
        this.canBeDyed = true;
        this.modelSupplier = () -> state -> switch (state) {
            case OFF -> modelOff;
            case LOW_POWER, ON -> modelOn;
            case BROKEN -> modelBroken;
            case LIGHT -> modelLight;
        };
        this.dyedModelSupplier = () -> state -> switch (state) {
            case OFF -> dyedModelOff;
            case LOW_POWER, ON -> dyedModelOn;
            case BROKEN -> dyedModelBroken;
            case LIGHT -> dyedModelLight;
            case BULB -> dyedModelBulb;
        };
        applyRatedValues(
                RATED_POWER_WATTS,
                ratedVoltageVolts,
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
        tooltipComponents.add(Component.translatable("powergrid.tooltip.holdForDescription",
                        Component.translatable("create.tooltip.keyShift"))
                .withStyle(ChatFormatting.DARK_GRAY));
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
