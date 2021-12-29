package com.denfop.integration.jei.genlava;


import com.denfop.api.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenLavaHandler {

    private static final List<GenLavaHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final int input;


    public static List<GenLavaHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }


    public GenLavaHandler(
            int input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }


    public int getEnergy() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return input2;
    }

    public static GenLavaHandler addRecipe(
            int input, FluidStack input2
    ) {
        GenLavaHandler recipe = new GenLavaHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenLavaHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenLavaHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }


    public static void initRecipes() {
        for (Map.Entry<NBTTagCompound, FluidStack> container :
                Recipes.lavagenrator.getRecipes().entrySet()) {
            addRecipe(container.getKey().getInteger("amount"), container.getValue());

        }
    }

    private static ItemStack is(Item item) { // Побочный метод.
        return new ItemStack(item);
    }

    private static ItemStack is(Block block) { // Побочный метод.
        return new ItemStack(block);
    }

}
