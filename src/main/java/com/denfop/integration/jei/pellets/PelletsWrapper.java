package com.denfop.integration.jei.pellets;

import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class PelletsWrapper implements IRecipeWrapper {


    private final double inputstack;
    private final ItemStack inputstack2;
    private final int col;

    public PelletsWrapper(PelletsHandler container) {


        this.inputstack = container.getInput();
        this.inputstack2 = container.getInput2();
        this.col = container.getCol();

    }

    public int getCol() {
        return col;
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
        final double num = 20000 / Math.ceil(this.inputstack);

        minecraft.fontRenderer.drawSplitString(
                String.format("%.2f☢ -> 50 EF", num),
                90,
                30,
                recipeWidth - 10,
                ModUtils.convertRGBcolorToInt(255, 255, 255)
        );
    }

}
