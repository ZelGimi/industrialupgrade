package com.denfop.integration.jei.probeassembler;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProbeAssemblerRecipeWrapper implements IRecipeWrapper {


    private final List<ItemStack> inputstack;
    private final ItemStack outputstack;

    public ProbeAssemblerRecipeWrapper(ProbeAssemblerHandler container) {


        this.inputstack = container.getInput();
        this.outputstack = container.getOutput();

    }

    public List<ItemStack> getInputs1() {
        return inputstack;
    }


    public List<ItemStack> getInputs() {

        return getInputs1();
    }

    public List<ItemStack> getOutputs() {
        return new ArrayList<>(Collections.singleton(this.outputstack));
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, this.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public ItemStack getOutput() {
        return outputstack;
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {


    }


}
