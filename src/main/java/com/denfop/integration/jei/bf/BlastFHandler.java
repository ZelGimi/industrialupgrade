package com.denfop.integration.jei.bf;


import com.denfop.IUItem;
import com.denfop.recipes.ItemStackHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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
        addRecipe(ItemStackHelper.fromData(Items.IRON_INGOT), IUItem.advIronIngot);
        addRecipe(ItemStackHelper.fromData(IUItem.iuingot, 1, 3), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 480));
        addRecipe(ItemStackHelper.fromData(IUItem.plastic_plate), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 479));

    }

    public ItemStack getStack() {
        return stack;
    }

    public ItemStack getOutput() {
        return output;
    }


}
