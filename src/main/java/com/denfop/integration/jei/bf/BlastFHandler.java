package com.denfop.integration.jei.bf;


import com.denfop.IUItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlastFHandler {

    private static final List<BlastFHandler> recipes = new ArrayList<>();
    private final ItemStack stack;
    private final ItemStack output;


    public BlastFHandler(ItemStack stack, ItemStack output) {
        this.stack = stack;
        this.output = output;
    }

    public static List<BlastFHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static BlastFHandler addRecipe(ItemStack stack, ItemStack output) {
        BlastFHandler recipe = new BlastFHandler(stack, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static BlastFHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (BlastFHandler recipe : recipes) {

            return recipe;

        }
        return null;
    }

    public static void initRecipes() {
        addRecipe(new ItemStack(Items.IRON_INGOT), IUItem.advIronIngot);
        addRecipe(new ItemStack(IUItem.iuingot, 1, 3), new ItemStack(IUItem.crafting_elements, 1, 480));
        addRecipe(new ItemStack(IUItem.plastic_plate), new ItemStack(IUItem.crafting_elements, 1, 479));

    }

    public ItemStack getStack() {
        return stack;
    }

    public ItemStack getOutput() {
        return output;
    }


}
