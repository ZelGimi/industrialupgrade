package com.denfop.integration.jei.aircollector;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AirColRecipeWrapper implements IRecipeWrapper {


    private final FluidStack inputstack;
    private final FluidStack outputstack;
    private final FluidStack outputstack1;

    public AirColRecipeWrapper(AirColHandler container) {


        this.inputstack = container.getOutput();
        this.outputstack1 = container.getOutput1();
        this.outputstack = container.getOutput2();

    }

    public FluidStack getInput() {
        return inputstack;
    }

    public FluidStack getOutput1() {
        return outputstack1;
    }


    public List<FluidStack> getOutputs() {
        List<FluidStack> lst = new ArrayList<>();
        lst.add(this.inputstack);
        lst.add(this.outputstack);
        lst.add(this.outputstack1);
        return lst;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.FLUID, this.getOutputs());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

}
