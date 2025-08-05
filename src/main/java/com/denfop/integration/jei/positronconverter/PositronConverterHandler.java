package com.denfop.integration.jei.positronconverter;


import com.denfop.IUItem;
import com.denfop.recipes.ItemStackHelper;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PositronConverterHandler {

    private static final List<PositronConverterHandler> recipes = new ArrayList<>();
    final ItemStack input1;
    final ItemStack input2;
    private final double input;


    public PositronConverterHandler(
            double input, ItemStack input2, ItemStack input1
    ) {
        this.input = input;
        this.input2 = input2;
        this.input1 = input1;
    }

    public static List<PositronConverterHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static PositronConverterHandler addRecipe(
            double input, ItemStack input2, ItemStack input1
    ) {
        PositronConverterHandler recipe = new PositronConverterHandler(input, input2, input1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static PositronConverterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (PositronConverterHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(2000, ItemStackHelper.fromData(IUItem.crafting_elements, 1, 352), ItemStackHelper.fromData(IUItem.proton));


    }


    public double getEnergy() {
        return input;
    }

    public ItemStack getOutput() {
        return input2;
    }

}
