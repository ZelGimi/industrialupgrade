package com.denfop.api;

import com.denfop.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

public interface IObsidianGenerator {

    /**
     * Adds a recipe to the machine.
     *
     * @param fluidStack  FluidStack to be filled
     * @param fluidStack1 FluidStack to fill into the container
     * @param output      Filled container
     */
    void addRecipe(FluidStack fluidStack, FluidStack fluidStack1, ItemStack output);

    /**
     * Gets the recipe output for the given input.
     *
     * @param input       FluidStack to be filled
     * @param input1      FluidStack to fill into the container
     * @param adjustInput modify the input according to the recipe's requirements
     * @param acceptTest  allow either container or fill to be null to see if either of them is part of a recipe
     * @return Recipe output, or null if none
     */
    RecipeOutput getOutputFor(FluidStack input, FluidStack input1, boolean adjustInput, boolean acceptTest);

    /**
     * Gets a list of recipes.
     * <p>
     * You're a mad evil scientist if you ever modify this.
     *
     * @return List of recipes
     */
    Map<Input, RecipeOutput> getRecipes();


    class Input {

        public final FluidStack fluidStack;
        public final FluidStack fluidStack1;

        public Input(FluidStack fluidStack, FluidStack fluidStack1) {
            this.fluidStack = fluidStack;
            this.fluidStack1 = fluidStack1;
        }

        public boolean matches(FluidStack fluidStack, FluidStack fluidStack1) {
            return fluidStack != null && fluidStack1 != null && this.fluidStack.isFluidEqual(fluidStack) && this.fluidStack1.isFluidEqual(
                    fluidStack1);
        }

    }

}
