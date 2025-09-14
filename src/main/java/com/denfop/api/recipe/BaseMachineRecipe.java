package com.denfop.api.recipe;


import net.minecraft.core.RegistryAccess;
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

    public static BaseMachineRecipe readNBT(CompoundTag tag,RegistryAccess access) {
        Input input = Input.readNBT(tag.getCompound("Input"), access);
        List<ItemStack> items = new ArrayList<>();
        ListTag itemsTag = tag.getList("Items", Tag.TAG_COMPOUND);
        for (Tag t : itemsTag) {
            if (t instanceof CompoundTag itemTag) {
                items.add(ItemStack.parseOptional(access,itemTag));
            }
        }
        CompoundTag metadata = tag.contains("Metadata", Tag.TAG_COMPOUND)
                ? tag.getCompound("Metadata")
                : null;

        RecipeOutput output = new RecipeOutput(metadata, items);
        return new BaseMachineRecipe(input, output);
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


    public CompoundTag writeNBT(RegistryAccess access) {
        CompoundTag tag = new CompoundTag();
        tag.put("Input", input.writeNBT(access));
        ListTag itemsTag = new ListTag();
        if (output != null)
            if (output.items != null)
                for (ItemStack stack : output.items) {
                    if (stack != null && !stack.isEmpty())
                        itemsTag.add(stack.save(access,new CompoundTag()));
                }
        if (!itemsTag.isEmpty())
        tag.put("Items", itemsTag);
        if (output != null && output.metadata != null) {
            tag.put("Metadata", output.metadata);
        }

        return tag;
    }
}
