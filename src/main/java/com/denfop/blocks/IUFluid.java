package com.denfop.blocks;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class IUFluid extends BaseFlowingFluid {


    private final boolean isSource;
    private final Properties fluidProperty = null;

    public IUFluid(Properties properties, boolean isSource) {
        super(properties);
        if (!isSource) {
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }
        this.isSource = isSource;

    }

    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        if (!isSource)
            builder.add(LEVEL);
    }

    public int getAmount(FluidState state) {
        if (!isSource)
            return state.getValue(LEVEL);
        else
            return 8;
    }

    public boolean isSource(FluidState state) {
        return isSource;
    }

    public Properties getFluidProperty() {
        return fluidProperty;
    }


}
