package com.denfop.api;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ISunnariumRecipeManager {

    /**
     * Adds a recipe to the machine.
     *
     * @param container Container to be filled
     * @param fill      Item to fill into the container
     * @param output    Filled container
     */
    void addRecipe(IRecipeInput container, IRecipeInput fill, IRecipeInput fill1, IRecipeInput fill2, ItemStack output);

    /**
     * Gets the recipe output for the given input.
     *
     * @param container   Container to be filled
     * @param fill        Item to fill into the container
     * @param adjustInput modify the input according to the recipe's requirements
     * @param acceptTest  allow either container or fill to be null to see if either of them is part of a recipe
     * @return Recipe output, or null if none
     */
    RecipeOutput getOutputFor(
            ItemStack container,
            ItemStack fill,
            ItemStack fill1,
            ItemStack fill2,
            boolean adjustInput,
            boolean acceptTest
    );

    /**
     * Gets a list of recipes.
     * <p>
     * You're a mad evil scientist if you ever modify this.
     *
     * @return List of recipes
     */
    Map<Input, RecipeOutput> getRecipes();


    class Input {

        public Input(IRecipeInput container1, IRecipeInput fill1, IRecipeInput fill2, IRecipeInput fill3) {
            this.container = container1;
            this.fill = fill1;
            this.fill2 = fill2;
            this.fill3 = fill3;
        }
        public List<IRecipeInput> getList(){
            List<IRecipeInput> lst = new ArrayList<>();
            lst.add(container);
            lst.add(fill);
            lst.add(fill2);
            lst.add(fill3);
            return  lst;
        }
        public boolean matches(ItemStack container1, ItemStack fill1, ItemStack fill2, ItemStack fill3) {
            return this.container.matches(container1) && this.fill.matches(fill1) && this.fill2.matches(fill2) && this.fill3.matches(
                    fill3);
        }

        public final IRecipeInput container;
        public final IRecipeInput fill;
        public final IRecipeInput fill2;
        public final IRecipeInput fill3;

    }

}
