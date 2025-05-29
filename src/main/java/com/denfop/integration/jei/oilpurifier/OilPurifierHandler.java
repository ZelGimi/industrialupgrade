package com.denfop.integration.jei.oilpurifier;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class OilPurifierHandler {

    private static final List<OilPurifierHandler> recipes = new ArrayList<>();
    private final ItemStack output;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;


    public OilPurifierHandler(ItemStack output, FluidStack inputFluid, FluidStack outputFluid) {
        this.output = output;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
    }

    public static List<OilPurifierHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }


    public static OilPurifierHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        for (BaseFluidMachineRecipe entry : Recipes.recipes.getRecipeFluid().getRecipeList(
                "oil_purifier")) {
            ItemStack output = entry.getOutput().items.get(0);
            FluidStack inputFluid = entry.input.getInputs().get(0);
            FluidStack outputFluid = entry.output_fluid.get(0);


            addRecipe(output,
                    inputFluid, outputFluid
            );
        }


    }

    private static OilPurifierHandler addRecipe(ItemStack output, FluidStack inputFluid, FluidStack outputFluid) {
        OilPurifierHandler recipe = new OilPurifierHandler(output, inputFluid, outputFluid);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public ItemStack getOutput() {
        return output;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

}
