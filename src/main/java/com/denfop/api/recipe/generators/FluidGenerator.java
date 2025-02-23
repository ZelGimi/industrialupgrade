package com.denfop.api.recipe.generators;

import net.minecraftforge.fluids.Fluid;

public class FluidGenerator {

    private final Fluid fluid;
    private final TypeGenerators typeGenerators;
    private final double energy;
    private final int amount;

    public FluidGenerator(Fluid fluid, int amount, double energy, TypeGenerators typeGenerators) {
        this.fluid = fluid;
        this.amount = amount;
        this.energy = energy;
        this.typeGenerators = typeGenerators;
    }

    public double getEnergy() {
        return energy;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public TypeGenerators getTypeGenerators() {
        return typeGenerators;
    }

    public int getAmount() {
        return amount;
    }

}
