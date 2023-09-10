package com.denfop.integration.jei;

import com.denfop.api.crafting.BaseRecipe;
import com.denfop.recipe.IInputItemStack;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseRecipeWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {

    private final BaseRecipe recipe;

    public BaseRecipeWrapper(BaseRecipe recipe) {
        this.recipe = recipe;
    }

    public static List<List<ItemStack>> replaceRecipeInputs(List<IInputItemStack> list) {
        List<List<ItemStack>> out = new ArrayList<>(list.size());
        for (IInputItemStack recipe : list) {
            if (recipe == null) {
                out.add(Collections.emptyList());
                continue;
            }
            List<ItemStack> replace = new ArrayList<>(recipe.getInputs());
            out.add(replace);
        }
        return out;
    }

    public List<List<ItemStack>> getInputs() {

        List<IInputItemStack> ret = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (this.recipe.getInputIndex()[i] == 1) {
                ret.add(this.recipe.getInput()[i]);
            } else {
                ret.add(null);
            }
        }
        return replaceRecipeInputs(ret);
    }

    public int getWidth() {
        return 3;
    }

    public int getHeight() {
        return 3;
    }

    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, getInputs());
        ingredients.setOutput(ItemStack.class, this.recipe.getOutput());
    }

}
