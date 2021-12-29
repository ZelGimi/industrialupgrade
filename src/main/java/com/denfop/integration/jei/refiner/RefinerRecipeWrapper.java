package com.denfop.integration.jei.refiner;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RefinerRecipeWrapper implements IRecipeWrapper {


    private final FluidStack inputstack;
    private final FluidStack outputstack;
    private final FluidStack outputstack1;

    public RefinerRecipeWrapper(RefinerHandler container) {


        this.inputstack = container.getInput();
        this.outputstack1 = container.getOutput1();
        this.outputstack = container.getOutput();

    }

    public FluidStack getInput() {
        return inputstack;
    }

    public FluidStack getOutput1() {
        return outputstack1;
    }

    public List<List<FluidStack>> getInputs() {
        FluidStack inputs = this.inputstack;
        List<FluidStack> stack = new ArrayList<>();
        stack.add(inputs);
        return Collections.singletonList(stack);
    }

    public List<FluidStack> getOutputs() {
        List<FluidStack> lst = new ArrayList<>();
        lst.add(this.outputstack);
        lst.add(this.outputstack1);
        return lst;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(FluidStack.class, this.getInputs());
        ingredients.setOutputs(FluidStack.class, this.getOutputs());
    }


    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
