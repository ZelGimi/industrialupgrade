package com.denfop.integration.jei.fishmachine;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class FishMWrapper implements IRecipeWrapper {


    private final ItemStack outputstack;


    public FishMWrapper(FishMHandler container) {


        this.outputstack = container.getOutput();

    }


    public ItemStack getOutputs() {

        return this.outputstack;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.ITEM, this.getOutput());
    }


    public ItemStack getOutput() {
        return outputstack;
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

}
