package com.denfop.integration.jei.doublemolecular;


import com.denfop.api.IDoubleMolecularRecipeManager;
import com.denfop.api.Recipes;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DoubleMolecularTransformerHandler {
    private static List< DoubleMolecularTransformerHandler> recipes = new ArrayList<>();
    private final double energy;

    public static List< DoubleMolecularTransformerHandler> getRecipes() { // Получатель всех рецептов.
        if(recipes.isEmpty())
            initRecipes();
        return recipes;
    }

    private final ItemStack input,input1, output;


    public DoubleMolecularTransformerHandler(ItemStack input, ItemStack input1,ItemStack output, double energy) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.energy = energy;
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
    public double getEnergy() { // Получатель выходного предмета рецепта.
        return energy;
    }


    public static DoubleMolecularTransformerHandler addRecipe(ItemStack input, ItemStack input1,ItemStack output, double energy) {
        DoubleMolecularTransformerHandler recipe = new DoubleMolecularTransformerHandler(input,input1, output, energy);
        if (recipes.contains(recipe))
            return null;
        recipes.add(recipe);
        return recipe;
    }

    public static DoubleMolecularTransformerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty())
            return null;
        for (DoubleMolecularTransformerHandler recipe : recipes)
            if (recipe.matchesInput(is))
                return recipe;
        return null;
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) ||is.isItemEqual(input1);
    }

    public static void initRecipes() {
        for (Map.Entry<IDoubleMolecularRecipeManager.Input, RecipeOutput> container :
                Recipes.doublemolecular.getRecipes().entrySet()) {
            addRecipe(container.getKey().container.getInputs().get(0),container.getKey().fill.getInputs().get(0),
                    container.getValue().items.get(0),
                    container.getValue().metadata.getDouble("energy"));

        }
    }

    private static ItemStack is(Item item) { // Побочный метод.
        return new ItemStack(item);
    }

    private static ItemStack is(Block block) { // Побочный метод.
        return new ItemStack(block);
    }
}
