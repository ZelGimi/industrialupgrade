package com.denfop.api.recipe;

import com.denfop.api.Recipes;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipeInputStack implements IRecipeInputStack {

    private final IInputItemStack input;

    public RecipeInputStack(IInputItemStack input) {
        this.input = input;

    }

    public RecipeInputStack(ItemStack input) {
        this.input = Recipes.inputFactory.getInput(Collections.singletonList(input));

    }

    @Override
    public List<ItemStack> getItemStack() {
        return input.getInputs();
    }

    @Override
    public boolean matched(final ItemStack stack) {
        for (ItemStack input : getItemStack()) {
            if (input.getItem() == stack.getItem()) {
                if (stack.getComponents().isEmpty() || input.getComponents().isEmpty()) {
                    return true;
                } else {
                    return ModUtils.checkItemEquality(input, stack);
                }
            }
        }
        return false;
    }

    @Override
    public IInputItemStack getInput() {
        return input;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeInputStack that = (RecipeInputStack) o;
        for (ItemStack input : getItemStack()) {

            for (ItemStack input1 : that.getItemStack()) {
                if (input.getItem() == input1.getItem()) {
                    if (input.getComponents().isEmpty()) {
                        return true;
                    } else {
                        return input.getComponents().equals(input1.getComponents());
                    }
                }
            }
        }
        return false;
    }


}
