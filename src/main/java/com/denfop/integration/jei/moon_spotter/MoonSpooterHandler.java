package com.denfop.integration.jei.moon_spotter;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MoonSpooterHandler {

    private static final List<MoonSpooterHandler> recipes = new ArrayList<>();
    private final ItemStack input,output;

    public MoonSpooterHandler(
            ItemStack input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<MoonSpooterHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static MoonSpooterHandler addRecipe(
            ItemStack input, ItemStack output
    ) {
        MoonSpooterHandler recipe = new MoonSpooterHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static MoonSpooterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (MoonSpooterHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("solar_glass_recipe")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items.get(0)
            );


        }
    }



    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }


    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) ;
    }

}
