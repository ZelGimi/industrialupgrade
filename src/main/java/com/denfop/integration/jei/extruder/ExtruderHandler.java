package com.denfop.integration.jei.extruder;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExtruderHandler {

    private static final List<ExtruderHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;
    private final BaseMachineRecipe container;

    public ExtruderHandler(ItemStack input, ItemStack output, BaseMachineRecipe container) {
        this.input = input;
        this.output = output;
        this.container = container;
    }

    public BaseMachineRecipe getContainer() {
        return container;
    }

    public static List<ExtruderHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ExtruderHandler addRecipe(ItemStack input, ItemStack output, BaseMachineRecipe container) {
        ExtruderHandler recipe = new ExtruderHandler(input, output,container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static ExtruderHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (ExtruderHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("extruding")) {

            try {
                addRecipe(
                        container.input.getInputs().get(0).getInputs().get(0),
                        container.getOutput().items.get(0),container
                );

            } catch (Exception ignored) {
                System.out.println(2);
            }
        }
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
