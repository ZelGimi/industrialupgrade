package com.denfop.integration.jei.replicator;

import com.denfop.Localization;
import com.denfop.blocks.FluidName;
import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ReplicatorWrapper implements IRecipeWrapper {


    private final double inputstack;
    private final ItemStack inputstack2;

    public ReplicatorWrapper(ReplicatorHandler container) {


        this.inputstack = container.getMatter();
        this.inputstack2 = container.getOutput();

    }

    public double getEnergy() {
        return inputstack;
    }

    public ItemStack getInput2() {
        return inputstack2;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.ITEM, this.inputstack2);
        ingredients.setInput(
                VanillaTypes.FLUID,
                new FluidStack(FluidName.fluiduu_matter.getInstance(), (int) Math.max(1, this.getEnergy()))
        );
    }


    public ItemStack getOutput() {
        return inputstack2;
    }

    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString(
                Localization.translate("cost.name") + "" + ModUtils.getString(getEnergy() / 1000) + "b",
                10,
                30,
                recipeWidth - 10,
                4210752
        );
    }

}
