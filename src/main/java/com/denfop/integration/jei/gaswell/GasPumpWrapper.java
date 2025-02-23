package com.denfop.integration.jei.gaswell;

import com.denfop.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

public class GasPumpWrapper implements IRecipeWrapper {


    private final FluidStack inputstack2;

    public GasPumpWrapper(GasPumpHandler container) {


        this.inputstack2 = container.getOutput();

    }

    public FluidStack getInput2() {
        return inputstack2;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.FLUID, this.inputstack2);
    }


    public FluidStack getOutput() {
        return inputstack2;
    }

    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString(
                Localization.translate("iu.gaspump.info"),
                10,
                5,
                recipeWidth - 10,
                4210752
        );
    }

}
