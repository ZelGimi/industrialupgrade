package com.denfop.integration.jei.genneutronium;


import com.denfop.api.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenNeuHandler {

    private static final List<GenNeuHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final double input;


    public GenNeuHandler(
            double input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenNeuHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenNeuHandler addRecipe(
            double input, FluidStack input2
    ) {
        GenNeuHandler recipe = new GenNeuHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenNeuHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenNeuHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        for (Map.Entry<NBTTagCompound, FluidStack> container :
                Recipes.neutroniumgenrator.getRecipes().entrySet()) {
            addRecipe(container.getKey().getDouble("amount"), container.getValue());

        }
    }

    private static ItemStack is(Item item) { // Побочный метод.
        return new ItemStack(item);
    }

    private static ItemStack is(Block block) { // Побочный метод.
        return new ItemStack(block);
    }

    public double getEnergy() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return input2;
    }

}
