package com.denfop.integration.jei.analyzer;

import com.denfop.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class AnalyzerWrapper implements IRecipeWrapper {

    private final ItemStack input;

    public AnalyzerWrapper(AnalyzerHandler container) {

        this.input = container.getInput();
    }

    public ItemStack getInput() {
        return input;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.ITEM, this.input);
    }


    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

        minecraft.fontRenderer.drawSplitString(Localization.translate("iu.analyzer.jei"), 5, 3,
                recipeWidth , 4210752
        );

    }

}
