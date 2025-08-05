package com.denfop.integration.jei.gaswell;


import com.denfop.blocks.FluidName;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GasPumpHandler {

    private static final List<GasPumpHandler> recipes = new ArrayList<>();
    static Random rand = new Random();
    private final FluidStack input2;

    public GasPumpHandler(
            FluidStack input2
    ) {
        this.input2 = input2;
    }

    public static List<GasPumpHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GasPumpHandler addRecipe(
            FluidStack input2
    ) {
        GasPumpHandler recipe = new GasPumpHandler(input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GasPumpHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GasPumpHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(new FluidStack(FluidName.fluidgas.getInstance().get(), 1000 + rand.nextInt(9001)));


    }


    public FluidStack getOutput() {
        return input2;
    }

}
