package com.denfop.integration.jei.smelteryfurnace;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class SmelteryFurnaceRecipeWrapper implements IRecipeWrapper {


    private final ItemStack input;
    private final FluidStack outputFluid;

    public SmelteryFurnaceRecipeWrapper(SmelteryFurnaceHandler container) {


        this.input = container.getInput();
        this.outputFluid = container.getOutputFluid();

    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }


    public ItemStack getInput() {
        return input;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.FLUID, this.getOutputFluid());
        ingredients.setInput(VanillaTypes.ITEM, this.getInput());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
