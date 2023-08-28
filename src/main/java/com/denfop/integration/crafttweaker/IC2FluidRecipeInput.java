package com.denfop.integration.crafttweaker;

import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class IC2FluidRecipeInput {

    private final ILiquidStack ingredient;

    public IC2FluidRecipeInput(ILiquidStack ingredient) {
        this.ingredient = ingredient;
    }

    public boolean matches(FluidStack subject) {
        return this.ingredient.matches(CraftTweakerMC.getILiquidStack(subject));
    }

    public int getAmount() {
        return this.ingredient.getAmount();
    }

    public FluidStack getInputs() {

        return CraftTweakerMC.getLiquidStack(this.ingredient);
    }

    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.ingredient != null ? this.ingredient.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            IC2FluidRecipeInput other = (IC2FluidRecipeInput) obj;
            return Objects.equals(this.ingredient, other.ingredient);
        }
    }

}
