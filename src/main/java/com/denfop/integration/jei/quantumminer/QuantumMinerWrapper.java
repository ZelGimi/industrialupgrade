package com.denfop.integration.jei.quantumminer;

import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class QuantumMinerWrapper implements IRecipeWrapper {


    private final double inputstack;
    private final ItemStack inputstack1;

    public QuantumMinerWrapper(QuantumMinerHandler container) {


        this.inputstack = container.getEnergy();
        this.inputstack1 = container.input1;

    }

    public double getEnergy() {
        return inputstack;
    }


    public ItemStack getInputstack() {
        return inputstack1;
    }

    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(inputstack1));
    }


    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString(
                ModUtils.getString(getEnergy()) + "QE",
                10,
                28,
                recipeWidth - 10,
                4210752
        );

    }

}
