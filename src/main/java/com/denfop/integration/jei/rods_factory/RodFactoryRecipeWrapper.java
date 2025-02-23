package com.denfop.integration.jei.rods_factory;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RodFactoryRecipeWrapper implements IRecipeWrapper {


    private final ItemStack outputstack;
    private final List<ItemStack> inputstacks;

    public RodFactoryRecipeWrapper(RodFactoryHandler container) {


        this.inputstacks = container.getInputs();
        this.outputstack = container.getOutput();

    }

    public List<ItemStack> getInputs1() {
        return inputstacks;
    }


    public List<ItemStack> getOutputs() {
        return new ArrayList<>(Collections.singleton(this.outputstack));
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, this.inputstacks);
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public ItemStack getOutput() {
        return outputstack;
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {


    }


}
