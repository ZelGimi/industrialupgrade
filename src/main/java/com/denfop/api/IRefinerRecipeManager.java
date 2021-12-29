package com.denfop.api;

import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

public interface IRefinerRecipeManager {

    /**
     * Adds a recipe to the machine.
     *
     * @param fluidStack FluidStack to be filled
     * @param output     Filled container
     */
    void addRecipe(FluidStack fluidStack, FluidStack[] output);

    /**
     * Gets the recipe output for the given input.
     *
     * @param input       FluidStack to be filled
     * @param adjustInput modify the input according to the recipe's requirements
     * @param acceptTest  allow either container or fill to be null to see if either of them is part of a recipe
     * @return Recipe output, or null if none
     */
    FluidStack[] getOutputFor(FluidStack input, boolean adjustInput, boolean acceptTest);

    /**
     * Gets a list of recipes.
     * <p>
     * You're a mad evil scientist if you ever modify this.
     *
     * @return List of recipes
     */
    Map<Input, FluidStack[]> getRecipes();


    class Input {

        public Input(FluidStack fluidStack) {
            this.fluidStack = fluidStack;
        }

        public boolean matches(FluidStack fluidStack) {
            return fluidStack != null && this.fluidStack.isFluidEqual(fluidStack);
        }

        public final FluidStack fluidStack;

    }

}
