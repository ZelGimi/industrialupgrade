package com.denfop.integration.jei.aircollector;


import com.denfop.blocks.FluidName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class AirColHandler {

    private static final List<AirColHandler> recipes = new ArrayList<>();
    private final FluidStack output1;
    private final FluidStack input, output;

    public AirColHandler(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        this.input = input;
        this.output1 = output1;
        this.output = output;
    }

    public static List<AirColHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static AirColHandler addRecipe(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        AirColHandler recipe = new AirColHandler(input, output, output1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static AirColHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {

        addRecipe(new FluidStack(FluidName.fluidazot.getInstance(), 6000),
                new FluidStack(
                        FluidName.fluidoxy.getInstance(),
                        2000
                ), new FluidStack(FluidName.fluidco2.getInstance(), 1000)
        );


    }


    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput2() { // Получатель входного предмета рецепта.
        return output;
    }

    public FluidStack getOutput1() { // Получатель выходного предмета рецепта.
        return output1;
    }

}
