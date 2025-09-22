package com.denfop.integration.jei.charged_redstone;

import com.denfop.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ChargedRedstoneWrapper implements IRecipeWrapper {

    private final ItemStack input;
    private final String output;

    public ChargedRedstoneWrapper(ChargedRedstoneHandler container) {

        this.input = container.input;
        this.output = container.output;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.ITEM, input);
    }


    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

        minecraft.fontRenderer.drawSplitString(Localization.translate(output), 5, 3,
                recipeWidth - 5, 4210752
        );

    }

}
