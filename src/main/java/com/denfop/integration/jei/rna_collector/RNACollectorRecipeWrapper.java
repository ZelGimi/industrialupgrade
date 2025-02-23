package com.denfop.integration.jei.rna_collector;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class RNACollectorRecipeWrapper implements IRecipeWrapper {


    private final ItemStack input;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;

    public RNACollectorRecipeWrapper(RNACollectorHandler container) {


        this.input = container.getInput();
        this.inputFluid = container.getInputFluid();
        this.outputFluid = container.getOutputFluid();

    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public ItemStack getInput() {
        return input;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, this.getInputFluid());
        ingredients.setOutput(VanillaTypes.FLUID, this.getOutputFluid());
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(this.getInput()));
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
