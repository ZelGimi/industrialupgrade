package com.denfop.integration.crafttweaker;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.denfop.recipe.IInputItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class InputItemStack implements IInputItemStack {

    List<ItemStack> stacks;

    public boolean matches(ItemStack subject) {
        List<ItemStack> inputs = stacks;
        Item subjectItem = subject.getItem();

        return inputs.stream()
                .anyMatch(oreStack -> {
                    Item oreItem = oreStack.getItem();
                    return subjectItem == oreItem;
                });
    }

    public int getAmount() {
        return stacks.get(0).getCount();
    }

    @Override
    public void growAmount(final int col) {
        stacks.forEach(stack -> stack.grow(col));
    }

    public List<ItemStack> getInputs() {

        return stacks;
    }

    @Override
    public boolean hasTag() {
        return false;
    }

    @Override
    public TagKey<Item> getTag() {
        return null;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {

    }

    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.stacks.hashCode();
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            InputItemStack other = (InputItemStack) obj;
            return Objects.equals(this.stacks, other.stacks);
        }
    }

}
