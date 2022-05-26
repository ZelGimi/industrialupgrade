package com.denfop.integration.jei.farmer;


import com.denfop.api.Recipes;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FarmerHandler {

    private static final List<FarmerHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;

    public FarmerHandler(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public static List<FarmerHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static FarmerHandler addRecipe(ItemStack input, ItemStack output) {
        FarmerHandler recipe = new FarmerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static FarmerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (FarmerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (MachineRecipe<IRecipeInput, Collection<ItemStack>> container : Recipes.fermer.getRecipes()) {
            addRecipe(container.getInput().getInputs().get(0), new ArrayList<>(container.getOutput()).get(0)
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

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == input.getItem();
    }

}
