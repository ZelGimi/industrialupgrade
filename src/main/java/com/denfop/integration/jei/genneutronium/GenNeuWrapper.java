package com.denfop.integration.jei.genneutronium;

import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

public class GenNeuWrapper implements  IRecipeWrapper  {




    private final double inputstack;
    private final FluidStack inputstack2;

    public GenNeuWrapper(GenNeuHandler container) {


        this.inputstack = container.getEnergy();
        this.inputstack2 = container.getOutput();

    }
    public double getEnergy() {
        return inputstack;
    }
    public FluidStack getInput2() {
        return inputstack2;
    }






    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(FluidStack.class,this.inputstack2);
    }


    public FluidStack getOutput() {
        return inputstack2;
    }

    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString(Localization.translate("cost.name") + " " + ModUtils.getString((double) getEnergy()) + "EU", 10, 30,recipeWidth - 10, 4210752);
    }
}
