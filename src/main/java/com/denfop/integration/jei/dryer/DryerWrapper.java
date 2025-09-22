package com.denfop.integration.jei.dryer;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DryerWrapper implements IRecipeWrapper {


    private final ItemStack outputstack;
    private final FluidStack inputstack;

    public DryerWrapper(DryerHandler container) {


        this.inputstack = container.getInput();
        this.outputstack = container.getOutput();

    }

    public FluidStack getInputstack() {
        return inputstack;
    }

    public ItemStack getOutputstack() {
        return outputstack;
    }

    public List<List<FluidStack>> getInputs() {
        FluidStack inputs = this.inputstack;
        List<FluidStack> stack = new ArrayList<>();
        stack.add(inputs);
        return Collections.singletonList(stack);
    }

    public List<ItemStack> getOutputs() {
        return new ArrayList<>(Collections.singleton(this.outputstack));
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.FLUID, this.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
