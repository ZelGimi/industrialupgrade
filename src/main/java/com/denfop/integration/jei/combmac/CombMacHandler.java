package com.denfop.integration.jei.combmac;


import com.denfop.api.Recipes;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CombMacHandler {

    private static final List<CombMacHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;

    public CombMacHandler(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public static List<CombMacHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CombMacHandler addRecipe(ItemStack input, ItemStack output) {
        CombMacHandler recipe = new CombMacHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static CombMacHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (CombMacHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (MachineRecipe<IRecipeInput, Collection<ItemStack>> container : Recipes.macerator.getRecipes()) {
            if (new ArrayList<>(container.getOutput()).size() > 0 && container.getInput().getInputs().size() > 0) {
                addRecipe(container.getInput().getInputs().get(0), new ArrayList<>(container.getOutput()).get(0)
                );
            }

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
