package com.denfop.api;


import com.denfop.mixin.access.IngredientAccessor;
import com.denfop.mixin.access.TagValueAccessor;
import com.denfop.recipe.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
        if (var1 instanceof IInputItemStack)
            return (IInputItemStack) var1;
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
        if (var1 instanceof Ingredient) {
            Ingredient ingredient = (Ingredient) var1;
            if (((IngredientAccessor) ingredient).getValues()[0] instanceof Ingredient.TagValue) {
                Ingredient.TagValue tagValue = (Ingredient.TagValue) ((IngredientAccessor) ingredient).getValues()[0];
                return this.getInput(((TagValueAccessor) tagValue).getTag());
            } else {
                return this.getInput(ingredient.getItems());
            }
        }
        if (var1 instanceof List) {
            List<?> list = (List<?>) var1;


            boolean allItemStacks = list.stream().allMatch(e -> e instanceof ItemStack);
            if (allItemStacks) {
                @SuppressWarnings("unchecked")
                List<ItemStack> itemStacks = (List<ItemStack>) list;
                return this.getInput(itemStacks.toArray(new ItemStack[0]));

            }
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
