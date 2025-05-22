package com.denfop.integration.jei.colonial_resource;

import com.denfop.Localization;
import com.denfop.api.space.IBody;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class SpaceColonyRecipeWrapper implements IRecipeWrapper {


    private final List<ItemStack> inputstack;
    private final List<FluidStack> outputstack;
    private final IBody body;

    public SpaceColonyRecipeWrapper(SpaceColonyHandler container) {

        this.body = container.body;
        this.inputstack = container.getInput();
        this.outputstack = container.getOutput();

    }

    public List<ItemStack> getInputs1() {
        return inputstack;
    }


    public List<ItemStack> getInputs() {

        return getInputs1();
    }

    public List<FluidStack> getOutputs() {
        return outputstack;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, this.getInputs());
        ingredients.setOutputs(VanillaTypes.FLUID, this.getOutputs());
    }


    public List<FluidStack> getOutput() {
        return outputstack;
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString(
                Localization.translate("iu.space_recipe.jei") + Localization.translate("iu.body." + body.getName().toLowerCase()),
                5,
                3,
                recipeWidth - 5,
                4210752
        );
        minecraft.fontRenderer.drawSplitString(
                Localization.translate("iu.space_recipe.jei2"), 5, 20,
                recipeWidth - 5, 4210752
        );

    }


}
