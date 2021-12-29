package com.denfop.api;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

import java.util.Map;

public interface ITripleMachineRecipeManager {

    /**
     * Adds a recipe to the machine.
     *
     * @param container Container to be filled
     * @param fill      Item to fill into the container
     * @param output    Filled container
     */
    void addRecipe(IRecipeInput container, IRecipeInput fill, IRecipeInput fill1, ItemStack output);

    /**
     * Gets the recipe output for the given input.
     *
     * @param container   Container to be filled
     * @param fill        Item to fill into the container
     * @param adjustInput modify the input according to the recipe's requirements
     * @param acceptTest  allow either container or fill to be null to see if either of them is part of a recipe
     * @return Recipe output, or null if none
     */
    RecipeOutput getOutputFor(ItemStack container, ItemStack fill, ItemStack fill1, boolean adjustInput, boolean acceptTest);

    /**
     * Gets a list of recipes.
     * <p>
     * You're a mad evil scientist if you ever modify this.
     *
     * @return List of recipes
     */
    Map<Input, RecipeOutput> getRecipes();


    class Input {

        public Input(IRecipeInput container1, IRecipeInput fill1, IRecipeInput fill2) {
            this.container = container1;
            this.fill = fill1;
            this.fill1 = fill2;
        }

        public boolean matches(ItemStack container1, ItemStack fill1, ItemStack fill2) {
            return this.container.matches(container1) && this.fill.matches(fill1) && this.fill1.matches(fill2);
        }

        public boolean matches1(ItemStack container1, ItemStack fill1, ItemStack fill2) {
            return this.container.matches(container1) && this.fill.matches(fill2) && this.fill1.matches(fill1);
        }

        public boolean matches2(ItemStack container1, ItemStack fill1, ItemStack fill2) {
            return this.container.matches(fill1) && this.fill.matches(container1) && this.fill1.matches(fill2);
        }

        public boolean matches3(ItemStack container1, ItemStack fill1, ItemStack fill2) {
            return this.container.matches(fill1) && this.fill.matches(fill2) && this.fill1.matches(container1);
        }

        public boolean matches4(ItemStack container1, ItemStack fill1, ItemStack fill2) {
            return this.container.matches(fill2) && this.fill.matches(fill1) && this.fill1.matches(container1);
        }

        public boolean matches5(ItemStack container1, ItemStack fill1, ItemStack fill2) {
            return this.container.matches(fill2) && this.fill.matches(container1) && this.fill1.matches(fill1);
        }

        public final IRecipeInput container;
        public final IRecipeInput fill;
        public final IRecipeInput fill1;

    }

}
