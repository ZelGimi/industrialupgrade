package com.denfop.integration.jei.waterrotorsupgrade;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WaterRotorUpgradeHandler implements IJeiVariantRecipe {

    private static final List<WaterRotorUpgradeHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
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
            JeiIngredientHelper.attachInputVariants(addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0)
            ), container);


        }
    }


    public ItemStack[] getInputs() { // Получатель входного предмета рецепта.
        return input;
    }




    @Override
    public void setInputVariants(final List<List<ItemStack>> inputVariants) {
        this.inputVariants = inputVariants == null ? new ArrayList<>() : inputVariants;
    }

    @Override
    public List<ItemStack> getInputVariants(final int slot, final ItemStack fallback) {
        return JeiIngredientHelper.getInputVariants(this.inputVariants, slot, fallback);
    }
}
