package com.denfop.integration.jei.genmatter;

import com.denfop.Localization;
import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

public class GenMatterWrapper implements IRecipeWrapper {


    private final int inputstack;
    private final FluidStack inputstack2;

    public GenMatterWrapper(GenMatterHandler container) {


        this.inputstack = container.getEnergy();
        this.inputstack2 = container.getOutput();

    }

    public int getEnergy() {
        return inputstack;
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
                Localization.translate("cost.name") + " " + ModUtils.getString((double) getEnergy()) + "EF",
                10,
                30,
                recipeWidth - 10,
                4210752
        );
    }

}
