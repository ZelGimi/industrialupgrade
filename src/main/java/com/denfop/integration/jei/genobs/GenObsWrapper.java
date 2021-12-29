package com.denfop.integration.jei.genobs;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GenObsWrapper implements  IRecipeWrapper  {




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
        ingredients.setInput(FluidStack.class, this.inputstack);
        ingredients.setInput(FluidStack.class, this.inputstack2);
        ingredients.setOutput(ItemStack.class, this.output);
    }




    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }
}
