package com.denfop.integration.jei.genradiation;

import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class GenRadWrapper implements IRecipeWrapper {


    private final double inputstack;
    private final ItemStack inputstack2;

    public GenRadWrapper(GenRadHandler container) {


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
        ingredients.setInput(VanillaTypes.ITEM, this.inputstack2);
    }


    public ItemStack getOutput() {
        return inputstack2;
    }

    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString(
                ModUtils.getString(getEnergy()) + "☢",
                70,
                60,
                recipeWidth - 10,
                4210752
        );

    }

}
