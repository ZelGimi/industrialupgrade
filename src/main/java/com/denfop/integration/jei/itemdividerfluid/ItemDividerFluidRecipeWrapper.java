package com.denfop.integration.jei.itemdividerfluid;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ItemDividerFluidRecipeWrapper implements IRecipeWrapper {


    private final ItemStack input;
    private final FluidStack output;
    private final FluidStack output1;

    public ItemDividerFluidRecipeWrapper(ItemDividerFluidHandler container) {


        this.input = container.getInput();
        this.output = container.getOutput();
        this.output1 = container.getOutput1();

    }

    public FluidStack getOutputFluid() {
        return output1;
    }


    public ItemStack getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.FLUID, Arrays.asList(this.getOutput(), this.getOutputFluid()));
        ingredients.setInput(VanillaTypes.ITEM, this.getInput());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
