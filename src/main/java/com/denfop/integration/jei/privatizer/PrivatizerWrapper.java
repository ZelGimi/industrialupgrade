package com.denfop.integration.jei.privatizer;

import com.denfop.IUItem;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PrivatizerWrapper implements IRecipeWrapper {


    private final ItemStack outputstack;


    public PrivatizerWrapper(PrivatizerHandler container) {


        this.outputstack = container.getOutput();

    }


    public List<ItemStack> getOutputs() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(IUItem.module7, 1, 0));
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
