package com.denfop.integration.jei.waterrotorsupgrade;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WaterRotorUpgradeHandler {

    private static final List<WaterRotorUpgradeHandler> recipes = new ArrayList<>();
    private final ItemStack[] input;

    public WaterRotorUpgradeHandler(ItemStack... inputs) {
        this.input = inputs;
    }

    public static List<WaterRotorUpgradeHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static WaterRotorUpgradeHandler addRecipe(ItemStack... inputs) {
        WaterRotorUpgradeHandler recipe = new WaterRotorUpgradeHandler(inputs);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("water_rotor_upgrade")) {
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
