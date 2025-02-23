package com.denfop.integration.jei.genradiation;


import com.denfop.IUItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GenRadHandler {

    private static final List<GenRadHandler> recipes = new ArrayList<>();
    private final ItemStack input2;
    private final double input;


    public GenRadHandler(
            double input, ItemStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenRadHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenRadHandler addRecipe(
            double input, ItemStack input2
    ) {
        GenRadHandler recipe = new GenRadHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenRadHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenRadHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(150, new ItemStack(IUItem.crafting_elements, 1, 443));


    }


    public double getEnergy() {
        return input;
    }

    public ItemStack getOutput() {
        return input2;
    }

}
