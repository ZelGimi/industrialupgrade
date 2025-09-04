package com.denfop.integration.jei.genred;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class GenRedHandler {

    private static final List<GenRedHandler> recipes = new ArrayList<>();
    private final ItemStack input2;
    private final double input;


    public GenRedHandler(
            double input, ItemStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenRedHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenRedHandler addRecipe(
            double input, ItemStack input2
    ) {
        GenRedHandler recipe = new GenRedHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenRedHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenRedHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(10000, new ItemStack(Items.REDSTONE));
        addRecipe(90000, new ItemStack(Blocks.REDSTONE_BLOCK));


    }


    public double getEnergy() {
        return input;
    }

    public ItemStack getOutput() {
        return input2;
    }

}
