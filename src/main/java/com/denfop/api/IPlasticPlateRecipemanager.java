package com.denfop.api;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

public interface IPlasticPlateRecipemanager {

    /**
     * Adds a recipe to the machine.
     *
     * @param container Container to be filled
     * @param output    Filled container
     */
    void addRecipe(IRecipeInput container, FluidStack fluidStack, ItemStack output);

    /**
     * Gets the recipe output for the given input.
     *
     * @param container   Container to be filled
     * @param adjustInput modify the input according to the recipe's requirements
     * @param acceptTest  allow either container or fill to be null to see if either of them is part of a recipe
     * @return Recipe output, or null if none
     */
    RecipeOutput getOutputFor(ItemStack container, FluidStack fluidStack, boolean adjustInput, boolean acceptTest);

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

        public Input(IRecipeInput container1, FluidStack fluidStack) {
            this.container = container1;

            this.fluidStack = fluidStack;
        }

        public boolean matches(ItemStack container1, FluidStack fluidStack) {
            return this.fluidStack.isFluidEqual(fluidStack) && fluidStack != null && this.container.matches(container1);
        }

        public final IRecipeInput container;

    }

}
