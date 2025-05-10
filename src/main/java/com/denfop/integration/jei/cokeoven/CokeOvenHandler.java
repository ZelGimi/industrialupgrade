package com.denfop.integration.jei.cokeoven;


import com.denfop.blocks.FluidName;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class CokeOvenHandler {

    private static final List<CokeOvenHandler> recipes = new ArrayList<>();
    private final ItemStack stack;
    private final FluidStack output;


    public CokeOvenHandler(ItemStack stack, FluidStack output) {
        this.stack = stack;
        this.output = output;
    }

    public static List<CokeOvenHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CokeOvenHandler addRecipe(ItemStack stack, FluidStack output) {
        CokeOvenHandler recipe = new CokeOvenHandler(stack, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static CokeOvenHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (CokeOvenHandler recipe : recipes) {

            return recipe;

        }
        return null;
    }

    public static void initRecipes() {
        addRecipe(new ItemStack(Items.COAL), new FluidStack(FluidName.fluidcreosote.getInstance().get(), 250));

    }

    public ItemStack getStack() {
        return stack;
    }

    public FluidStack getOutput() {
        return output;
    }


}
