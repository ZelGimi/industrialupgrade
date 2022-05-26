package com.denfop.integration.jei.gense;


import com.denfop.api.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenSEHandler {

    private static final List<GenSEHandler> recipes = new ArrayList<>();
    private final ItemStack output;

    public GenSEHandler(ItemStack output) {
        this.output = output;
    }

    public static List<GenSEHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenSEHandler addRecipe(ItemStack output) {
        GenSEHandler recipe = new GenSEHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenSEHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenSEHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        for (Map.Entry<NBTTagCompound, ItemStack> container : Recipes.sunnarium.getRecipes().entrySet()) {
            addRecipe(container.getValue());

        }
    }

    private static ItemStack is(Item item) { // Побочный метод.
        return new ItemStack(item);
    }

    private static ItemStack is(Block block) { // Побочный метод.
        return new ItemStack(block);
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

}
