package com.denfop.integration.jei.smeltery_controller;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class SmelteryControllerRecipeWrapper implements IRecipeWrapper {


    private final List<FluidStack> input;
    private final FluidStack outputFluid;

    public SmelteryControllerRecipeWrapper(SmelteryControllerHandler container) {


        this.input = container.getInput();
        this.outputFluid = container.getOutputFluid();

    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }


    public List<FluidStack> getInput() {
        return input;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.FLUID, this.getOutputFluid());
        ingredients.setInputs(VanillaTypes.FLUID, this.getInput());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
