package com.denfop.integration.jei.electrolyzer;


import com.denfop.blocks.FluidName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ElectrolyzerHandler {

    private static final List<ElectrolyzerHandler> recipes = new ArrayList<>();
    private final FluidStack output1;
    private final FluidStack input, output;

    public ElectrolyzerHandler(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        this.input = input;
        this.output1 = output1;
        this.output = output;
    }

    public static List<ElectrolyzerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ElectrolyzerHandler addRecipe(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        ElectrolyzerHandler recipe = new ElectrolyzerHandler(input, output, output1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static ElectrolyzerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {

        addRecipe(new FluidStack(FluidRegistry.WATER, 1000), new FluidStack(
                        FluidName.fluidhyd.getInstance(),
                        500
                ),
                new FluidStack(FluidName.fluidoxy.getInstance(), 250)
        );


    }


    public FluidStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return output;
    }

    public FluidStack getOutput1() { // Получатель выходного предмета рецепта.
        return output1;
    }

}
