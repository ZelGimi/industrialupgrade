package com.denfop.api.storage.autocrafting;

import com.denfop.utils.ModUtils;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class SameStack {

    public static CompoundTag EMPTY = new CompoundTag();
    public String key = "";
    private ItemStack stack = ItemStack.EMPTY;
    private FluidStack fluidStack = FluidStack.EMPTY;

    public SameStack() {
    }

    public SameStack(ItemStack stack) {
        this.stack = stack;
    }

    public SameStack(FluidStack stack) {
        this.fluidStack = stack;
    }

    public static SameStack readFromNBT(CompoundTag nbt) {
        SameStack sameStack = new SameStack();

        if (nbt.contains("Item")) {
            sameStack.stack = ItemStack.of(nbt.getCompound("Item"));
        }
        if (nbt.contains("Fluid")) {
            sameStack.fluidStack = FluidStack.loadFluidStackFromNBT(nbt.getCompound("Fluid"));
        }
        int amount = nbt.getInt("count");
        if (sameStack.isItem()) {
            if (!sameStack.isFluid())
                sameStack.stack.setCount(amount);
            else
                sameStack.stack.setCount(1);
        }
        if (sameStack.isFluid()) {
            sameStack.fluidStack.setAmount(amount);
        }
        return sameStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SameStack sameStack = (SameStack) o;
        if (sameStack.stack.isEmpty() && this.stack.isEmpty() && !fluidStack.isEmpty() && !sameStack.fluidStack.isEmpty())
            return fluidStack.getFluid() == sameStack.fluidStack.getFluid();
        if (!sameStack.stack.isEmpty() && !this.stack.isEmpty() && fluidStack.isEmpty() && sameStack.fluidStack.isEmpty())
            return ModUtils.checkItemEquality(stack, sameStack.stack) && ModUtils.checkNbtEquality(stack.getTag(), sameStack.stack.getTag());
        return false;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    @Override
    public int hashCode() {
        if (this.isItem())
            return Objects.hash(stack.getItem(), this.getTag());
        else
            return Objects.hash(fluidStack.getFluid());
    }

    public boolean isCorrect(FluidStack stack) {
        return fluidStack.getFluid() == stack.getFluid();
    }

    public boolean isCorrect(ItemStack stack) {
        return ModUtils.checkItemEquality(stack, this.stack) && ModUtils.checkNbtEquality(this.stack.getTag(), stack.getTag());
    }

    public CompoundTag getTag() {
        if (this.isItem())
            return this.stack.getTag() == null ? EMPTY : this.stack.getTag();
        return EMPTY;
    }

    public void addCount(int amount) {
        this.addCount(amount, false);
    }

    public void addCount(int amount, boolean ignoreFluid) {
        if (ignoreFluid) {
            if (!stack.isEmpty())
                stack.setCount(Math.max(1, stack.getCount() + amount));
            else
                fluidStack.setAmount(Math.max(1, fluidStack.getAmount() + amount));
        } else {
            if (!fluidStack.isEmpty())
                fluidStack.setAmount(Math.max(1, fluidStack.getAmount() + amount));
            else
                stack.setCount(Math.max(1, stack.getCount() + amount));
        }
    }

    public int getAmount() {

        return this.getAmount(false);
    }

    public void setAmount(int amount) {
        if (this.isItem()) {
            this.stack.setCount(amount);
        } else {
            this.fluidStack.setAmount(amount);
        }
    }

    public int getAmount(boolean ignoreFluid) {
        if (ignoreFluid) {
            if (stack.isEmpty())
                return fluidStack.getAmount();
            return stack.getCount();
        } else {
            if (fluidStack.isEmpty())
                return stack.getCount();
            return fluidStack.getAmount();
        }
    }

    public CompoundTag writeToNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("count", this.getAmount());
        if (!stack.isEmpty()) {
            if (stack.getCount() > 127)
                stack.setCount(127);
            nbt.put("Item", stack.save(new CompoundTag()));
        }
        if (!fluidStack.isEmpty()) {
            nbt.put("Fluid", fluidStack.writeToNBT(new CompoundTag()));
        }

        return nbt;
    }

    public boolean isFluid() {
        return !fluidStack.isEmpty();
    }

    public boolean isItem() {
        return !stack.isEmpty();
    }

    public String getKey() {
        if (key.isEmpty()) {
            if (this.isItem())
                key = Registry.ITEM.getKey(this.stack.getItem()).toString();
            else
                key = Registry.FLUID.getKey(this.getFluidStack().getFluid()).toString();
        }
        return key;
    }

    public SameStack copy() {
        if (this.isItem()) {
            return new SameStack(this.stack.copy());
        } else {
            return new SameStack(this.fluidStack.copy());
        }
    }

    public SameStack copyWithFluid() {
        SameStack sameStack = new SameStack(this.stack.copy());
        sameStack.setFluidStack(this.fluidStack.copy());
        return sameStack;

    }

    public void setAmount(int amount, boolean ignoreFluid) {
        if (ignoreFluid) {
            this.setAmount(amount);
        } else {
            if (this.isFluid()) {
                this.fluidStack.setAmount(amount);

            } else {
                this.stack.setCount(amount);
            }
        }
    }

    public boolean isEmpty() {
        return !this.isItem() && !this.isFluid();
    }
}
