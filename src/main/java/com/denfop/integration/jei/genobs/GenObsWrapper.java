package com.denfop.integration.jei.genobs;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class GenObsWrapper implements IRecipeWrapper {


    private final FluidStack inputstack;
    private final FluidStack inputstack2;
    private final ItemStack output;

    public GenObsWrapper(GenObsHandler container) {


        this.inputstack = container.getInput();
        this.inputstack2 = container.getInput1();
        this.output = container.getOutput();

    }

    public FluidStack getInput1() {
        return inputstack;
    }

    public FluidStack getInput2() {
        return inputstack2;
    }

    public ItemStack getOutput() {
        return output;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, this.inputstack);
        ingredients.setInput(VanillaTypes.FLUID, this.inputstack2);
        ingredients.setOutput(VanillaTypes.ITEM, this.output);
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

}
