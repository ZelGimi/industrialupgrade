package com.denfop.integration.jei.heavyanvil;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HeavyAnvilHandler {

    private static final List<HeavyAnvilHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final ItemStack output;
    private final BaseMachineRecipe container;


    public HeavyAnvilHandler(ItemStack input, ItemStack output, BaseMachineRecipe container) {
        this.input = input;
        this.output = output;
        this.container = container;
    }

    public static List<HeavyAnvilHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static HeavyAnvilHandler addRecipe(
            ItemStack input, ItemStack output,
            BaseMachineRecipe container) {
        HeavyAnvilHandler recipe = new HeavyAnvilHandler(input, output, container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("strong_anvil")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items.get(0), container
            );


        }
    }

    public BaseMachineRecipe getContainer() {
        return container;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }


}
