package com.denfop.integration.jei.convertermatter;


import com.denfop.api.Recipes;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConverterHandler {
    private static List<ConverterHandler> recipes = new ArrayList<>();


    public static List<ConverterHandler> getRecipes() { // Получатель всех рецептов.
        if(recipes.isEmpty())
            initRecipes();
        return recipes;
    }

    private final ItemStack  output;


    public ConverterHandler(ItemStack output) {
        this.output = output;
    }



    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }


    public static ConverterHandler addRecipe(ItemStack output) {
        ConverterHandler recipe = new ConverterHandler(output);
        if (recipes.contains(recipe))
            return null;
        recipes.add(recipe);
        return recipe;
    }

    public static ConverterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty())
            return null;
        for (ConverterHandler recipe : recipes)
            return recipe;
        return null;
    }


    public static void initRecipes() {
        for (MachineRecipe<IRecipeInput, Collection<ItemStack>> container : Recipes.matterrecipe.getRecipes()) {
            addRecipe(new ArrayList<>(container.getOutput()).get(0));

        }
    }

    private static ItemStack is(Item item) { // Побочный метод.
        return new ItemStack(item);
    }

    private static ItemStack is(Block block) { // Побочный метод.
        return new ItemStack(block);
    }
}
