package com.denfop.integration.jei.pellets;


import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityPalletGenerator;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PelletsHandler {

    private static final List<PelletsHandler> recipes = new ArrayList<>();
    private final ItemStack input2;
    private final double input;
    private final int col;


    public PelletsHandler(
            double input, ItemStack input2, int col
    ) {
        this.input = input;
        this.input2 = input2;
        this.col = col;
    }

    public static List<PelletsHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static PelletsHandler addRecipe(
            double input, ItemStack input2, int col
    ) {
        PelletsHandler recipe = new PelletsHandler(input, input2, col);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static PelletsHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (PelletsHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 6; j++) {
                ItemStack stack = new ItemStack(IUItem.pellets.getStack(i), 1);
                double coef = 1;
                for (Map.Entry<ItemStack, Double> entry : BlockEntityPalletGenerator.integerMap.entrySet()) {
                    if (entry.getKey().is(stack.getItem())) {
                        coef = entry.getValue();
                    }
                }
                double coef1 = Math.pow(coef, j + 1);
                addRecipe(
                        coef1,
                        stack,
                        j + 1
                );
            }

        }
        for (int i = 8; i < 10; i++) {
            for (int j = 0; j < 6; j++) {
                ItemStack stack = new ItemStack(IUItem.nuclear_res.getStack(i), 1);
                double coef = 1;
                for (Map.Entry<ItemStack, Double> entry : BlockEntityPalletGenerator.integerMap.entrySet()) {
                    if (entry.getKey().is(stack.getItem())) {
                        coef = entry.getValue();
                    }
                }
                double coef1 = Math.pow(coef, j + 1);
                addRecipe(
                        coef1,
                        stack,
                        j + 1
                );
            }

        }

    }

    public double getInput() {
        return input;
    }

    public int getCol() {
        return col;
    }

    public ItemStack getInput2() {
        return input2;
    }


}
