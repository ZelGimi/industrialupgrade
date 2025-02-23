package com.denfop.integration.jei;

import com.denfop.api.crafting.BaseShapelessRecipe;
import com.denfop.recipe.IInputItemStack;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BaseShapelessWrapper extends BlankRecipeWrapper {

    private final BaseShapelessRecipe recipe;

    public BaseShapelessWrapper(BaseShapelessRecipe recipe) {
        this.recipe = recipe;
    }

    public List<List<ItemStack>> getInputs() {
        List<List<ItemStack>> ret = new ArrayList<>(this.recipe.getRecipeInputList().size());
        for (IInputItemStack input : this.recipe.getRecipeInputList()) {
            ret.add(input.getInputs());
        }
        return ret;
    }

    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, getInputs());
        ingredients.setOutput(ItemStack.class, this.recipe.getOutput());
    }

}
