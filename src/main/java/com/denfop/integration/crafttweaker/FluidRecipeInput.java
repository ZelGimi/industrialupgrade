package com.denfop.integration.crafttweaker;


import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class FluidRecipeInput {

    private final IFluidStack ingredient;

    public FluidRecipeInput(IFluidStack ingredient) {
        this.ingredient = ingredient;
    }

    public boolean matches(FluidStack subject) {
        return this.ingredient.asFluidIngredient().matches(subject.getFluid());
    }

    public int getAmount() {
        return this.ingredient.getAmount();
    }

    public FluidStack getInputs() {

        return this.ingredient.getInternal();
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
            FluidRecipeInput other = (FluidRecipeInput) obj;
            return Objects.equals(this.ingredient, other.ingredient);
        }
    }

}
