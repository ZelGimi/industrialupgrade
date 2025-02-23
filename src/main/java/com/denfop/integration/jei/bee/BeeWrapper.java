package com.denfop.integration.jei.bee;

import com.denfop.IUItem;
import com.denfop.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class BeeWrapper implements IRecipeWrapper {

    private final ItemStack input;
    private final ItemStack output;

    public BeeWrapper(BeeHandler container) {

        this.input = container.getInput();
        this.output = container.getOutput();
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, this.input);
        ingredients.setInput(VanillaTypes.ITEM, new ItemStack(IUItem.net));
        ingredients.setOutput(VanillaTypes.ITEM, this.output);
    }


    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {


        minecraft.fontRenderer.drawSplitString("+", 27, 28,
                recipeWidth - 5, 4210752
        );
        minecraft.fontRenderer.drawSplitString("->", 51, 28,
                recipeWidth - 5, 4210752
        );

    }

}
