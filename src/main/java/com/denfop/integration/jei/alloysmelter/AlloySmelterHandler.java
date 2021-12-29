package com.denfop.integration.jei.alloysmelter;


import com.denfop.api.IDoubleMachineRecipeManager;
import com.denfop.api.Recipes;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlloySmelterHandler {
    private static List< AlloySmelterHandler> recipes = new ArrayList<>();


    public static List< AlloySmelterHandler> getRecipes() { // Получатель всех рецептов.
        if(recipes.isEmpty())
            initRecipes();
        return recipes;
    }

    private final ItemStack input,input1, output;


    public AlloySmelterHandler(ItemStack input, ItemStack input1,ItemStack output) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
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

    public static AlloySmelterHandler addRecipe(ItemStack input, ItemStack input1,ItemStack output) {
        AlloySmelterHandler recipe = new AlloySmelterHandler(input,input1, output);
        if (recipes.contains(recipe))
            return null;
        recipes.add(recipe);
        return recipe;
    }

    public static AlloySmelterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty())
            return null;
        for (AlloySmelterHandler recipe : recipes)
            if (recipe.matchesInput(is))
                return recipe;
        return null;
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) ||is.isItemEqual(input1);
    }

    public static void initRecipes() {
        for (Map.Entry<IDoubleMachineRecipeManager.Input, RecipeOutput> container :
                Recipes.Alloysmelter.getRecipes().entrySet()) {
            addRecipe(container.getKey().container.getInputs().get(0),container.getKey().fill.getInputs().get(0),
                    container.getValue().items.get(0));

        }
    }

    private static ItemStack is(Item item) { // Побочный метод.
        return new ItemStack(item);
    }

    private static ItemStack is(Block block) { // Побочный метод.
        return new ItemStack(block);
    }
}
