package com.denfop.integration.jei.oilpurifier;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class OilPurifierRecipeWrapper implements IRecipeWrapper {


    private final ItemStack output;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;

    public OilPurifierRecipeWrapper(OilPurifierHandler container) {
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


    public ItemStack getOutput() {
        return output;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, this.getInputFluid());
        ingredients.setOutput(VanillaTypes.FLUID, this.getOutputFluid());
        ingredients.setOutput(VanillaTypes.ITEM, this.getOutput());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
