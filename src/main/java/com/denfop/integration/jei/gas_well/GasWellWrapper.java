package com.denfop.integration.jei.gas_well;

import com.denfop.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class GasWellWrapper implements IRecipeWrapper {

    private final Fluid input;
    private final String output;

    public GasWellWrapper(GasWellHandler container) {

        this.input = container.input;
        this.output = container.output;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.FLUID, new FluidStack(this.input,1));
    }


    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

        minecraft.fontRenderer.drawSplitString(Localization.translate(output), 5, 3,
                recipeWidth - 5, 4210752
        );

    }

}
