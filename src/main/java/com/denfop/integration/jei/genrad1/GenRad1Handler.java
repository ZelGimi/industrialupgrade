package com.denfop.integration.jei.genrad1;


import com.denfop.IUItem;
import com.denfop.recipes.ItemStackHelper;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GenRad1Handler {

    private static final List<GenRad1Handler> recipes = new ArrayList<>();
    private final ItemStack input2;
    private final double input;


    public GenRad1Handler(
            double input, ItemStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenRad1Handler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenRad1Handler addRecipe(
            double input, ItemStack input2
    ) {
        GenRad1Handler recipe = new GenRad1Handler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenRad1Handler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenRad1Handler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(150, ItemStackHelper.fromData(IUItem.crafting_elements, 1, 443));


    }


    public double getEnergy() {
        return input;
    }

    public ItemStack getOutput() {
        return input2;
    }

}
