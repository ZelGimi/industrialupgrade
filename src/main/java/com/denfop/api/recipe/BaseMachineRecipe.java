package com.denfop.api.recipe;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BaseMachineRecipe {

    public final IInput input;
    public final RecipeOutput output;

    public BaseMachineRecipe(IInput input, RecipeOutput output) {
        this.input = input;
        this.output = output;
    }

    public boolean matches(List<ItemStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            if (this.input.getInputs().get(i).matches(stacks.get(i))) {
                return true;
            }
        }
        return false;
    }

    public RecipeOutput getOutput() {
        return this.output;
    }
    public static BaseMachineRecipe readNBT(CompoundTag tag) {
        Input input = Input.readNBT(tag.getCompound("Input"));
        List<ItemStack> items = new ArrayList<>();
        ListTag itemsTag = tag.getList("Items", Tag.TAG_COMPOUND);
        for (Tag t : itemsTag) {
            if (t instanceof CompoundTag itemTag) {
                items.add(ItemStack.of(itemTag));
            }
        }
        CompoundTag metadata = tag.contains("Metadata", Tag.TAG_COMPOUND)
                ? tag.getCompound("Metadata")
                : null;

        RecipeOutput output = new RecipeOutput(metadata, items);
        return new BaseMachineRecipe(input, output);
    }

    public CompoundTag writeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("Input", input.writeNBT());
        ListTag itemsTag = new ListTag();
        if (output != null)
            if (output.items != null)
                for (ItemStack stack : output.items) {
                    if (stack != null)
                        itemsTag.add(stack.save(new CompoundTag()));
                }
        tag.put("Items", itemsTag);
        if (output != null && output.metadata != null) {
            tag.put("Metadata", output.metadata);
        }

        return tag;
    }
}
