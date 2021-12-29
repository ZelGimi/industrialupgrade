package com.denfop.api;

import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

public interface IFluidRecipeManager {
    /**
     * Adds a recipe to the machine.
     *
     * @param fluidStack FluidStack to be filled
     * @param output     Filled container
     */
    void addRecipe(FluidStack fluidStack, FluidStack[] output);


    /**
     * Gets a list of recipes.
     * <p>
     * You're a mad evil scientist if you ever modify this.
     *
     * @return List of recipes
     */
    Map<Input, FluidStack[]> getRecipes();


    class Input {
        public final FluidStack fluidStack;

        public Input(FluidStack fluidStack) {
            this.fluidStack = fluidStack;
        }

        public boolean matches(FluidStack fluidStack) {
            return fluidStack != null && this.fluidStack.isFluidEqual(fluidStack);
        }
    }
}
