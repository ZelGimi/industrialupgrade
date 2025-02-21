package com.denfop.integration.jei.advrefiner;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class AdvRefinerHandler {

    private static final List<AdvRefinerHandler> recipes = new ArrayList<>();
    private final FluidStack output1;
    private final FluidStack input, output;

    public AdvRefinerHandler(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        this.input = input;
        this.output1 = output1;
        this.output = output;
    }

    public static List<AdvRefinerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static AdvRefinerHandler addRecipe(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        AdvRefinerHandler recipe = new AdvRefinerHandler(input, output, output1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static AdvRefinerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {

        for (BaseFluidMachineRecipe machineRecipe : Recipes.recipes.getRecipeFluid().getRecipeList(
                "adv_oil_refiner")) {
            FluidStack fluidStack = machineRecipe.getInput().getInputs().get(0);
            FluidStack fluidStack1 = machineRecipe.getOutput_fluid().get(0);
            FluidStack fluidStack2 = machineRecipe.getOutput_fluid().get(1);
            double m = 1000D / fluidStack.amount;
            addRecipe(new FluidStack(fluidStack.getFluid(), (int) (m * fluidStack.amount)), new FluidStack(
                            fluidStack1.getFluid(),
                            (int) (m * fluidStack1.amount)
                    ),
                    new FluidStack(
                            fluidStack2.getFluid(),
                            (int) (m * fluidStack2.amount)
                    )
            );
        }


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
