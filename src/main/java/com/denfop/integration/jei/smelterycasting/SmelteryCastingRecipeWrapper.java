package com.denfop.integration.jei.smelterycasting;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class SmelteryCastingRecipeWrapper implements IRecipeWrapper {


    private final ItemStack input;
    private final ItemStack output;
    private final FluidStack inputFluid;

    public SmelteryCastingRecipeWrapper(SmelteryCastingHandler container) {


        this.input = container.getInput();
        this.output = container.getOutput();
        this.inputFluid = container.getInputFluid();

    }


    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, this.getInputFluid());
        ingredients.setInput(VanillaTypes.ITEM, this.getInput());
        ingredients.setOutput(VanillaTypes.ITEM, this.getOutput());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
