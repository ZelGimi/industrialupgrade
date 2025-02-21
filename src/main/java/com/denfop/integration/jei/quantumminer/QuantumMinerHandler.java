package com.denfop.integration.jei.quantumminer;


import com.denfop.IUItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuantumMinerHandler {

    private static final List<QuantumMinerHandler> recipes = new ArrayList<>();
    private static int[] metas = new int[]{637, 638, 639, 640, 643, 644, 648, 649};
    final ItemStack input1;
    private final double input;


    public QuantumMinerHandler(
            double input, ItemStack input1
    ) {
        this.input = input;
        this.input1 = input1;
    }

    public static List<QuantumMinerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static QuantumMinerHandler addRecipe(
            double input, ItemStack input1
    ) {
        QuantumMinerHandler recipe = new QuantumMinerHandler(input, input1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static QuantumMinerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (QuantumMinerHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        for (int meta : metas) {
            addRecipe(62500, new ItemStack(IUItem.crafting_elements, 1, meta));
        }


    }

    public ItemStack getInput1() {
        return input1;
    }

    public double getEnergy() {
        return input;
    }


}
