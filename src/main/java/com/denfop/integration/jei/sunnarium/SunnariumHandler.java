package com.denfop.integration.jei.sunnarium;


import com.denfop.api.ISunnariumRecipeManager;
import com.denfop.api.Recipes;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SunnariumHandler {

    private static final List<SunnariumHandler> recipes = new ArrayList<>();
    private final ItemStack input, input1, input2, input3, output;

    public SunnariumHandler(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3,
            ItemStack output
    ) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.output = output;
    }

    public static List<SunnariumHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static SunnariumHandler addRecipe(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3,
            ItemStack output
    ) {
        SunnariumHandler recipe = new SunnariumHandler(input, input1, input2, input3, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static SunnariumHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (SunnariumHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (Map.Entry<ISunnariumRecipeManager.Input, RecipeOutput> container :
                Recipes.sunnurium.getRecipes().entrySet()) {
            addRecipe(container.getKey().container.getInputs().get(0), container.getKey().fill.getInputs().get(0),
                    container.getKey().fill2.getInputs().get(0), container.getKey().fill3.getInputs().get(0),

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

    public ItemStack getInput2() { // Получатель входного предмета рецепта.
        return input2;
    }

    public ItemStack getInput3() { // Получатель входного предмета рецепта.
        return input3;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1) || is.isItemEqual(input2) || is.isItemEqual(input3);
    }

}
