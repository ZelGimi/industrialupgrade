package com.denfop.integration.jei.fluidsolidmixer;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class FluidSolidMixerRecipeWrapper implements IRecipeWrapper {


    private final ItemStack input;
    private final FluidStack outputFluid1;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;

    public FluidSolidMixerRecipeWrapper(FluidSolidMixerHandler container) {


        this.input = container.getInput();
        this.outputFluid1 = container.getOutputFluid1();
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

    public FluidStack getOutputFluid1() {
        return outputFluid1;
    }

    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, this.getInputFluid());
        ingredients.setOutputs(VanillaTypes.FLUID, Arrays.asList(this.getOutputFluid(), this.getOutputFluid1()));
        ingredients.setInput(VanillaTypes.ITEM, this.getInput());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
