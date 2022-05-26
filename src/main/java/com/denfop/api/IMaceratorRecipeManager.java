package com.denfop.api;

import ic2.api.recipe.IRecipeInput;

import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

public interface IMaceratorRecipeManager {

    /**
     * Adds a recipe to the machine.
     *
     * @param container Container to be filled
     * @param put    Filled container
     */
    void addRecipe(IRecipeInput container, NBTTagCompound metadata, ItemStack put);

    /**
     * Gets the recipe put for the given input.
     *
     * @param container   Container to be filled
     * @param adjustInput modify the input according to the recipe's requirements
     * @param acceptTest  allow either container or fill to be null to see if either of them is part of a recipe
     * @return Recipe put, or null if none
     */

    /**
     * Gets a list of recipes.
     * <p>
     * You're a mad evil scientist if you ever modify this.
     *
     * @return List of recipes
     */
    Map<Input, RecipeOutput> getRecipes();

    RecipeOutput getOutputFor(ItemStack stack1, boolean b, boolean b1);


    class Input {

        public final IRecipeInput container;


        public Input(IRecipeInput container1) {
            this.container = container1;
        }

        public boolean matches(ItemStack container1) {
            return this.container.matches(container1);
        }

        public IRecipeInput getContainer() {
            return container;
        }

    }

}
