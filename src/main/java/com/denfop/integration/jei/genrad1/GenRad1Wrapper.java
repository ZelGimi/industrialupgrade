package com.denfop.integration.jei.genrad1;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class GenRad1Wrapper implements IRecipeWrapper {


    private final double inputstack;
    private final ItemStack inputstack2;

    public GenRad1Wrapper(GenRad1Handler container) {


        this.inputstack = container.getEnergy();
        this.inputstack2 = container.getOutput();

    }

    public double getEnergy() {
        return inputstack;
    }

    public ItemStack getInput2() {
        return inputstack2;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.ITEM, this.inputstack2);
    }


    public ItemStack getOutput() {
        return inputstack2;
    }

    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {


    }

}
