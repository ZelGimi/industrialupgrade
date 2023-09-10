package com.denfop.integration.jei.recycler;


import com.denfop.IUItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class RecyclerHandler {

    private static final List<RecyclerHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;

    public RecyclerHandler(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public static List<RecyclerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RecyclerHandler addRecipe(ItemStack input, ItemStack output) {
        RecyclerHandler recipe = new RecyclerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static RecyclerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (RecyclerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (Item container : ForgeRegistries.ITEMS) {


            addRecipe(
                    new ItemStack(container),
                    IUItem.scrap
            );


        }
        for (Block container : ForgeRegistries.BLOCKS) {


            addRecipe(
                    new ItemStack(container),
                    IUItem.scrap
            );


        }
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
