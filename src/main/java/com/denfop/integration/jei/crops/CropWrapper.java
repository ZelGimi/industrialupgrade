package com.denfop.integration.jei.crops;

import com.denfop.api.agriculture.ICrop;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CropWrapper implements IRecipeWrapper {


    public final ICrop output;

    public CropWrapper(CropHandler container) {


        this.output = container.output;

    }

    public ICrop getOutput() {
        return output;
    }

    public ItemStack getOutputs() {
        return output.getDrop().isEmpty() ? ItemStack.EMPTY : output.getDrop().get(0);
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.ITEM, this.getOutputs());
        ingredients.setInput(VanillaTypes.ITEM, this.getInputs());
    }

    public ItemStack getInputs() {

        return output.getStack();
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

        minecraft.fontRenderer.drawString("->", 65, 48, 4210752);
    }

}
