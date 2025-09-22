package com.denfop.integration.jei.solidmatters;

import com.denfop.Localization;
import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatterWrapper implements IRecipeWrapper {


    private final ItemStack inputstack;
    private final int percent;


    public MatterWrapper(MatterHandler container) {


        this.inputstack = container.getInput();
        this.percent = container.getEnergy();

    }

    public ItemStack getInput() {
        return inputstack;
    }

    public int getPercent() {
        return percent;
    }


    public List<ItemStack> getOutputs() {
        return new ArrayList<>(Collections.singleton(this.inputstack));
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString(
                Localization.translate("cost.name") + " " + ModUtils.getString((double) percent) + "EF",
                10,
                67,
                recipeWidth - 10,
                4210752
        );


    }

}
