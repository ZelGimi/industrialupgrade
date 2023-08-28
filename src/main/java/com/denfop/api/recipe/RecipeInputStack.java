package com.denfop.api.recipe;

import com.denfop.recipe.IInputItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;
import java.util.List;

public class RecipeInputStack implements IRecipeInputStack {

    private final List<ItemStack> input;

    public RecipeInputStack(IInputItemStack input) {
        this.input = input.getInputs();

    }

    public RecipeInputStack(ItemStack input) {
        this.input = Collections.singletonList(input);

    }

    @Override
    public List<ItemStack> getItemStack() {
        return input;
    }

    @Override
    public boolean matched(final ItemStack stack) {
        for (ItemStack input : getItemStack()) {
            if (input.getItem() == stack.getItem() && (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack.getItemDamage() == input.getItemDamage())) {
                if (stack.getTagCompound() == null) {
                    return true;
                } else {
                    return stack.getTagCompound().equals(input.getTagCompound());
                }
            }
        }
        return false;
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
                if (input.getItem() == input1.getItem() && (input1.getItemDamage() == OreDictionary.WILDCARD_VALUE || input.getItemDamage() == input1.getItemDamage())) {
                    if (input.getTagCompound() == null) {
                        return true;
                    } else {
                        return input.getTagCompound().equals(input1.getTagCompound());
                    }
                }
            }
        }
        return false;
    }


}
