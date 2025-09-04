package com.denfop.integration.jei.cutting;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CuttingHandler {

    private static final List<CuttingHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;
    private final BaseMachineRecipe container;

    public CuttingHandler(ItemStack input, ItemStack output, BaseMachineRecipe container) {
        this.input = input;
        this.output = output;
        this.container = container;
    }

    public static List<CuttingHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CuttingHandler addRecipe(ItemStack input, ItemStack output, BaseMachineRecipe container) {
        CuttingHandler recipe = new CuttingHandler(input, output, container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static CuttingHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (CuttingHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("cutting")) {

            try {
                addRecipe(
                        container.input.getInputs().get(0).getInputs().get(0),
                        container.getOutput().items.get(0), container
                );
            } catch (Exception e) {
                System.out.println(2);
            }
            ;

        }
    }

    public BaseMachineRecipe getContainer() {
        return container;
    }

    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == input.getItem();
    }

}
