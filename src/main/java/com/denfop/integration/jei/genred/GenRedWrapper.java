package com.denfop.integration.jei.genred;

import com.denfop.Localization;
import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class GenRedWrapper implements IRecipeWrapper {


    private final double inputstack;
    private final ItemStack inputstack2;

    public GenRedWrapper(GenRedHandler container) {


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
                Localization.translate("iu.windgenerator1"),
                10,
                30,
                recipeWidth - 10,
                4210752
        );
        minecraft.fontRenderer.drawSplitString(
                ModUtils.getString(getEnergy()),
                10,
                30 + 7,
                recipeWidth - 10,
                4210752
        );
    }

}
