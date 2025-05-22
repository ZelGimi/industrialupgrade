package com.denfop.integration.jei.heavyanvil;

import com.denfop.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class HeavyAnvilWrapper implements IRecipeWrapper {

    private final ItemStack input;
    private final ItemStack output;

    public HeavyAnvilWrapper(HeavyAnvilHandler container) {

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
        ingredients.setOutput(VanillaTypes.ITEM, this.output);
    }


    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

        minecraft.fontRenderer.drawSplitString(Localization.translate("iu.anvil.jei"), 5, 3,
                recipeWidth - 5, 4210752
        );
        minecraft.fontRenderer.drawSplitString("+", 26, 31,
                recipeWidth - 5, 4210752
        );
        minecraft.fontRenderer.drawSplitString("->", 47, 31,
                recipeWidth - 5, 4210752
        );
        minecraft.fontRenderer.drawSplitString(Localization.translate("iu.anvil.jei1") + "." + Localization.translate(
                        "iu.anvil.jei2"), 5, 50,
                recipeWidth - 5, 4210752
        );
    }

}
