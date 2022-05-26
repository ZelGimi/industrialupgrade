package com.denfop.integration.jei.molecular;


import com.denfop.api.Recipes;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MolecularTransformerHandler {

    private static final List<MolecularTransformerHandler> recipes = new ArrayList<MolecularTransformerHandler>();
    private final double energy;
    private final ItemStack input, output;

    public MolecularTransformerHandler(ItemStack input, ItemStack output, double energy) {
        this.input = input;
        this.output = output;
        this.energy = energy;
    }

    public static List<MolecularTransformerHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static MolecularTransformerHandler addRecipe(ItemStack input, ItemStack output, double energy) {
        MolecularTransformerHandler recipe = new MolecularTransformerHandler(input, output, energy);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static MolecularTransformerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (MolecularTransformerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (MachineRecipe<IRecipeInput, Collection<ItemStack>> container : Recipes.molecular.getRecipes()) {
            addRecipe(container.getInput().getInputs().get(0), new ArrayList<ItemStack>(container.getOutput()).get(0),
                    container.getMetaData().getDouble("energy")
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

    public double getEnergy() { // Получатель выходного предмета рецепта.
        return energy;
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == input.getItem();
    }

}
