package com.denfop.integration.jei.oilpump;

import ic2.core.init.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

public class OilPumpWrapper implements IRecipeWrapper {


    private final FluidStack inputstack2;

    public OilPumpWrapper(OilPumpHandler container) {


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
                Localization.translate("iu.oilpump.info"),
                10,
                30,
                recipeWidth - 10,
                4210752
        );
    }

}
