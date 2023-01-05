package com.denfop.integration.jei.rotorsupgrade;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RotorUpgradeHandler {

    private static final List<RotorUpgradeHandler> recipes = new ArrayList<>();
    private final ItemStack[] input;

    public RotorUpgradeHandler(ItemStack... inputs) {
        this.input = inputs;
    }

    public static List<RotorUpgradeHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RotorUpgradeHandler addRecipe(ItemStack... inputs) {
        RotorUpgradeHandler recipe = new RotorUpgradeHandler(inputs);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("rotor_upgrade")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0)
            );


        }
    }


    public ItemStack[] getInputs() { // Получатель входного предмета рецепта.
        return input;
    }


}
