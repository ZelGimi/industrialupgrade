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
        final int damage = stack.getItemDamage();
        for (ItemStack input : getItemStack()) {
            if (input.getItem() == stack.getItem() && (damage == OreDictionary.WILDCARD_VALUE || damage == input.getItemDamage())) {
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
            final int damage = input.getItemDamage();
            for (ItemStack input1 : that.getItemStack()) {
                final int damage1 = input1.getItemDamage();
                if (input.getItem() == input1.getItem() && (damage1 == OreDictionary.WILDCARD_VALUE || damage == damage1)) {
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
