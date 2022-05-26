package com.denfop.integration.jei.enrichment;


import com.denfop.api.IDoubleMachineRecipeManager;
import com.denfop.api.Recipes;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnrichHandler {

    private static final List<EnrichHandler> recipes = new ArrayList<>();
    private final ItemStack input, input1, output;

    public EnrichHandler(ItemStack input, ItemStack input1, ItemStack output) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
    }

    public static List<EnrichHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static EnrichHandler addRecipe(ItemStack input, ItemStack input1, ItemStack output) {
        EnrichHandler recipe = new EnrichHandler(input, input1, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static EnrichHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (EnrichHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (Map.Entry<IDoubleMachineRecipeManager.Input, RecipeOutput> container :
                Recipes.enrichment.getRecipes().entrySet()) {
            addRecipe(container.getKey().container.getInputs().get(0), container.getKey().fill.getInputs().get(0),
                    container.getValue().items.get(0)
            );

        }
    }

    private static ItemStack is(Item item) { // Побочный метод.
        return new ItemStack(item);
    }

    private static ItemStack is(Block block) { // Побочный метод.
        return new ItemStack(block);
    }

    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getInput1() { // Получатель входного предмета рецепта.
        return input1;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1);
    }

}
