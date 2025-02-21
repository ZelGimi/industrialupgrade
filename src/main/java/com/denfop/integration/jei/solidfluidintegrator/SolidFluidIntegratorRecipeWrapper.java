package com.denfop.integration.jei.solidfluidintegrator;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SolidFluidIntegratorRecipeWrapper implements IRecipeWrapper {


    private final FluidStack input;
    private final ItemStack output;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;

    public SolidFluidIntegratorRecipeWrapper(SolidFluidIntegratorHandler container) {


        this.input = container.getInput();
        this.output = container.getOutput();
        this.inputFluid = container.getInputFluid();
        this.outputFluid = container.getOutputFluid();

    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public FluidStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.FLUID, Arrays.asList(this.getInputFluid(), this.input));
        ingredients.setOutput(VanillaTypes.FLUID, this.getOutputFluid());
        ingredients.setOutput(VanillaTypes.ITEM, this.getOutput());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
