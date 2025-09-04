package com.denfop.recipe;


import com.denfop.api.item.energy.EnergyItem;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class InputItemStack implements IInputItemStack {
    public static InputItemStack EMPTY = new InputItemStack(ItemStack.EMPTY, 1, true);
    public final ItemStack input;
    public int amount;
    public InputItemStack(ItemStack input) {
        this(input, ModUtils.getSize(input));
    }

    public InputItemStack(ItemStack input, int amount) {
        if (ModUtils.isEmpty(input)) {
            throw new IllegalArgumentException("invalid input stack");
        } else {
            this.input = input.copy();
            this.amount = amount;
        }
    }

    InputItemStack(ItemStack input, int amount, boolean f) {
        this.input = input.copy();
        this.amount = amount;
    }

    public InputItemStack(CompoundTag compoundTag) {
        boolean exist = compoundTag.getBoolean("exist");
        if (exist) {
            this.input = ItemStack.of(compoundTag.getCompound("input"));
            this.amount = compoundTag.getInt("amount");
        } else {
            this.input = ItemStack.EMPTY;
            this.amount = 1;
        }
    }

    public static IInputItemStack create(CompoundTag tag) {
        byte id = tag.getByte("id");
        if (id == 0)
            return new InputItemStack(tag);
        if (id == 1)
            return new InputOreDict(tag);
        return new InputFluidStack(tag);
    }

    @Override
    public void growAmount(final int col) {
        this.amount++;
        this.input.setCount(amount);
    }

    public boolean matches(ItemStack subject) {
        boolean energy = (this.input.getItem() instanceof EnergyItem && subject.getItem() instanceof EnergyItem);
        return subject.getItem() == this.input.getItem() && ModUtils.checkItemEquality(this.input, subject);
    }

    public int getAmount() {
        return this.amount;
    }

    public List<ItemStack> getInputs() {
        return Collections.singletonList(ModUtils.setSize(this.input, this.getAmount()));
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
    public CompoundTag writeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putByte("id", (byte) 0);
        compoundTag.putBoolean("exist", input != null && !input.isEmpty());
        if (input != null && !input.isEmpty()) {
            compoundTag.putInt("amount", amount);
            compoundTag.put("input", input.save(new CompoundTag()));
        }
        return compoundTag;
    }

    public String toString() {
        return "RInputItemStack<" + ModUtils.setSize(this.input, this.amount) + ">";
    }

    public boolean equals(Object obj) {
        InputItemStack other;
        return obj != null && this.getClass() == obj.getClass() && ModUtils.checkItemEqualityStrict(
                (other = (InputItemStack) obj).input,
                this.input
        ) && other.amount == this.amount;
    }

}
