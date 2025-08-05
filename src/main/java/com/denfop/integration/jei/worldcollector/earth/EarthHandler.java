package com.denfop.integration.jei.worldcollector.earth;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EarthHandler {

    private static final List<EarthHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;
    private final double need;

    public EarthHandler(
            ItemStack input,
            ItemStack output,
            double need
    ) {
        this.input = input;
        this.output = output;
        this.need = need;
    }

    public static List<EarthHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static EarthHandler addRecipe(
            ItemStack input,
            ItemStack output,
            double need
    ) {
        EarthHandler recipe = new EarthHandler(input, output, need);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static EarthHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (EarthHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("earthcollector")) {
            try {
                addRecipe(container.input.getInputs().get(0).getInputs().get(0),

                        container.getOutput().items.get(0), container.getOutput().metadata.getDouble("need")
                );
            } catch (Exception e) {
                System.out.println(2);
            }
        }
    }

    public double getNeed() {
        return need;
    }

    public ItemStack getInput() {
        return input;
    }


    public ItemStack getOutput() {
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }

}
