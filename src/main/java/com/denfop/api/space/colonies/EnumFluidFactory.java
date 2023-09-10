package com.denfop.api.space.colonies;

import com.denfop.blocks.FluidName;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public enum EnumFluidFactory {
    LAVA(10, new FluidStack(FluidRegistry.LAVA, 1), 5, 10),
    WATER(10, new FluidStack(FluidRegistry.WATER, 1), 4, 8),
    OIL(12, new FluidStack(FluidName.fluidneft.getInstance(), 1), 6, 12),
    PETROL(14, new FluidStack(FluidName.fluidbenz.getInstance(), 1), 8, 16),
    DIESEL(14, new FluidStack(FluidName.fluiddizel.getInstance(), 1), 8, 16),
    COOLANT(14, new FluidStack(FluidName.fluidcoolant.getInstance(), 1), 8, 16);
    private final int energy;
    private final FluidStack stack;
    private final int needPeople;
    private final int needProtection;

    EnumFluidFactory(int energy, FluidStack stack, int needPeople, int needProtection) {
        this.energy = energy;
        this.stack = stack;
        this.needPeople = needPeople;
        this.needProtection = needProtection;
    }

    public static EnumFluidFactory getID(int id) {
        return values()[id % values().length];
    }

    public int getEnergy() {
        return energy;
    }

    public FluidStack getStack() {
        return stack;
    }

    public int getNeedPeople() {
        return needPeople;
    }

    public int getNeedProtection() {
        return needProtection;
    }
}
