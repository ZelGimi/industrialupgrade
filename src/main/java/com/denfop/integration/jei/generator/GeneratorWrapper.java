package com.denfop.integration.jei.generator;

import com.denfop.Localization;
import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

public class GeneratorWrapper implements IRecipeWrapper {


    private final double inputstack;
    private final FluidStack inputstack2;

    public GeneratorWrapper(GeneratorHandler container) {


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
        ingredients.setInput(VanillaTypes.FLUID, this.inputstack2);
    }


    public FluidStack getOutput() {
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
