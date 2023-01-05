package com.denfop.integration.jei.tuner;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TunerWrapper implements IRecipeWrapper {


    private final ItemStack outputstack;


    public TunerWrapper(TunerHandler container) {


        this.outputstack = container.getOutput();

    }


    public List<ItemStack> getOutputs() {
        List<ItemStack> list = new ArrayList<>();
        list.add(this.getOutput());
        return list;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, this.getOutputs());


    }


    public ItemStack getOutput() {
        return outputstack;
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

}
