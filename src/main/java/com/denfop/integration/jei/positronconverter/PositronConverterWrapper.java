package com.denfop.integration.jei.positronconverter;

import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class PositronConverterWrapper implements IRecipeWrapper {


    private final double inputstack;
    private final ItemStack inputstack2;
    private final ItemStack inputstack1;

    public PositronConverterWrapper(PositronConverterHandler container) {


        this.inputstack = container.getEnergy();
        this.inputstack2 = container.getOutput();
        this.inputstack1 = container.input1;

    }

    public double getEnergy() {
        return inputstack;
    }

    public ItemStack getInput2() {
        return inputstack2;
    }

    public ItemStack getInputstack() {
        return inputstack1;
    }

    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(this.inputstack2, inputstack1));
    }


    public ItemStack getOutput() {
        return inputstack2;
    }

    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString(
                ModUtils.getString(getEnergy()) + "e⁺",
                110,
                28,
                recipeWidth - 10,
                4210752
        );

    }

}
