package com.denfop.integration.jei.advalloysmelter;


import com.denfop.api.ITripleMachineRecipeManager;
import com.denfop.api.Recipes;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdvAlloySmelterHandler {

    private static final List<AdvAlloySmelterHandler> recipes = new ArrayList<>();


    public static List<AdvAlloySmelterHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    private final ItemStack input, input1, input2, output;


    public AdvAlloySmelterHandler(ItemStack input, ItemStack input1, ItemStack input2, ItemStack output) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
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

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public static AdvAlloySmelterHandler addRecipe(ItemStack input, ItemStack input1, ItemStack input2, ItemStack output) {
        AdvAlloySmelterHandler recipe = new AdvAlloySmelterHandler(input, input1, input2, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static AdvAlloySmelterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (AdvAlloySmelterHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1) || is.isItemEqual(input2);
    }

    public static void initRecipes() {
        for (Map.Entry<ITripleMachineRecipeManager.Input, RecipeOutput> container :
                Recipes.Alloyadvsmelter.getRecipes().entrySet()) {
            addRecipe(container.getKey().container.getInputs().get(0), container.getKey().fill.getInputs().get(0),
                    container.getKey().fill1.getInputs().get(0),
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

}
