package com.denfop.integration.jei.chemicalplant;

import com.denfop.Localization;
import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChemicalPlantWrapper implements IRecipeWrapper {


    private final FluidStack outputstack;
    private final FluidStack inputstack;

    public ChemicalPlantWrapper(ChemicalPlantHandler container) {


        this.inputstack = container.getInput();
        this.outputstack = container.getOutput();

    }

    public FluidStack getInputstack() {
        return inputstack;
    }

    public FluidStack getOutputstack() {
        return outputstack;
    }

    public List<List<FluidStack>> getInputs() {
        FluidStack inputs = this.inputstack;
        List<FluidStack> stack = new ArrayList<>();
        stack.add(inputs);
        return Collections.singletonList(stack);
    }

    public List<FluidStack> getOutputs() {
        return new ArrayList<>(Collections.singleton(this.outputstack));
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.FLUID, this.getInputs());
        ingredients.setOutputs(VanillaTypes.FLUID, this.getOutputs());
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString(
                Localization.translate("cost.name") + " " + ModUtils.getString((double) 5) + " QE",
                10,
                65,
                recipeWidth - 10,
                4210752
        );
    }

}
