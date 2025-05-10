package com.denfop.api;


import com.denfop.recipe.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public class InputHandler implements IInputHandler {

    public InputHandler() {
    }

    public static IInputItemStack fromNetwork(FriendlyByteBuf buffer) {
        int type = buffer.readInt();
        return switch (type) {
            case 0 -> new InputItemStack(buffer.readItem());
            case 1 -> new InputOreDict(buffer);
            case 2 -> new InputFluidStack(buffer);
            default -> null;
        };
    }

    public IInputItemStack getInput(ItemStack stack) {
        return new InputItemStack(stack);
    }

    @Override
    public IInputItemStack getInput(ItemStack[] stacks) {
        if (stacks.length == 1)
            return new InputItemStack(stacks[0]);
        else {
            TagKey<Item> tag = stacks[0].getTags().toList().get(0);
            return new InputOreDict(tag, stacks[0].getCount());
        }
    }

    @Override
    public IInputItemStack getInput(final Object var1) {
        if (var1 instanceof ItemStack)
            return this.getInput((ItemStack) var1);
        if (var1 instanceof Fluid)
            return this.getInput((Fluid) var1);
        if (var1 instanceof String)
            return this.getInput((String) var1);
        if (var1 instanceof Item)
            return this.getInput(new ItemStack((Item) var1));
        if (var1 instanceof TagKey)
            return this.getInput((TagKey<Item>) var1);
        if (var1 instanceof List) {
            List<TagKey<Item>> var2 = (List<TagKey<Item>>) var1;
            TagKey<Item> mainTag = var2.get(0);
            for (TagKey<Item> tagKey : var2) {
                StringBuilder location = new StringBuilder(tagKey.location().toString());
                if (location.indexOf("/") != -1) {
                    mainTag = tagKey;
                    break;
                }
            }
            return this.getInput(mainTag.location());
        }
        return null;
    }

    @Override
    public IInputItemStack getInput(final Object var1, int i) {
        if (var1 instanceof ItemStack)
            return this.getInput((ItemStack) var1, i);
        if (var1 instanceof Fluid)
            return this.getInput((Fluid) var1, i);
        if (var1 instanceof String)
            return this.getInput((String) var1, i);
        if (var1 instanceof TagKey)
            return this.getInput((TagKey<Item>) var1, i);
        return null;
    }

    public IInputItemStack getInput(TagKey<Item> name, int i) {
        return new InputOreDict(name, i);
    }

    public IInputItemStack getInput(TagKey<Item> name) {
        return new InputOreDict(name, 1);
    }

    public IInputItemStack getInput(ItemStack stack, int amount) {
        return new InputItemStack(stack, amount);
    }


    public IInputItemStack getInput(String name) {
        return new InputOreDict(name);
    }

    public IInputItemStack getInput(String name, int amount) {
        return new InputOreDict(name, amount);
    }

    public IInputItemStack getInput(String name, int amount, int metaOverride) {
        return new InputOreDict(name, amount, metaOverride);
    }

    public IInputItemStack getInput(Fluid fluid) {
        return new InputFluidStack(fluid);
    }

    public IInputItemStack getInput(Fluid fluid, int amount) {
        return new InputFluidStack(fluid, amount);
    }

}
