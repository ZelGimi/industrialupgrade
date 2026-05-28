package com.denfop.api.storage.autocrafting;

import com.denfop.utils.ModUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SameStack {

    public static DataComponentMap EMPTY = new DataComponentMap() {
        @Nullable
        @Override
        public <T> T get(DataComponentType<? extends T> dataComponentType) {
            return null;
        }

        @Override
        public Set<DataComponentType<?>> keySet() {
            return new HashSet<>();
        }
    };

    public String key = "";
    private ItemStack stack = ItemStack.EMPTY;
    private FluidStack fluidStack = FluidStack.EMPTY;

    public SameStack() {
    }

    public SameStack(ItemStack stack) {
        this.stack = stack == null ? ItemStack.EMPTY : stack;
    }

    public SameStack(FluidStack stack) {
        this.fluidStack = stack == null ? FluidStack.EMPTY : stack;
    }

    private static int safeAmount(int amount) {
        return Math.max(1, amount);
    }

    private static ItemStack copyForNbt(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = stack.copy();


        copy.setCount(1);

        return copy;
    }

    public static SameStack readFromNBT(CompoundTag nbt, HolderLookup.Provider registryAccess) {
        SameStack sameStack = new SameStack();

        int amount = safeAmount(nbt.getInt("count"));

        if (nbt.contains("Item")) {
            ItemStack parsedStack = ItemStack.parseOptional(registryAccess, nbt.getCompound("Item"));

            if (!parsedStack.isEmpty()) {
                parsedStack.setCount(amount);
                sameStack.stack = parsedStack;
            }
        }

        if (nbt.contains("Fluid")) {
            FluidStack parsedFluid = FluidStack.parseOptional(registryAccess, nbt.getCompound("Fluid"));

            if (!parsedFluid.isEmpty()) {
                parsedFluid.setAmount(amount);
                sameStack.fluidStack = parsedFluid;
            }
        }


        if (sameStack.isItem() && sameStack.isFluid()) {
            sameStack.stack.setCount(1);
        }

        return sameStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SameStack sameStack = (SameStack) o;

        if (sameStack.stack.isEmpty()
                && this.stack.isEmpty()
                && !fluidStack.isEmpty()
                && !sameStack.fluidStack.isEmpty()) {
            return fluidStack.getFluid() == sameStack.fluidStack.getFluid();
        }

        if (!sameStack.stack.isEmpty()
                && !this.stack.isEmpty()
                && fluidStack.isEmpty()
                && sameStack.fluidStack.isEmpty()) {
            return ModUtils.checkItemEquality(stack, sameStack.stack)
                    && ModUtils.checkNbtEquality(stack.getComponents(), sameStack.stack.getComponents());
        }

        return false;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack == null ? ItemStack.EMPTY : stack;
        this.key = "";
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidStack = fluidStack == null ? FluidStack.EMPTY : fluidStack;
        this.key = "";
    }

    @Override
    public int hashCode() {
        if (this.isItem()) {
            return Objects.hash(stack.getItem(), this.getTag());
        } else {
            return Objects.hash(fluidStack.getFluid());
        }
    }

    public boolean isCorrect(FluidStack stack) {
        return stack != null && !stack.isEmpty() && fluidStack.getFluid() == stack.getFluid();
    }

    public boolean isCorrect(ItemStack stack) {
        return stack != null
                && !stack.isEmpty()
                && !this.stack.isEmpty()
                && ModUtils.checkItemEquality(stack, this.stack)
                && ModUtils.checkNbtEquality(this.stack.getComponents(), stack.getComponents());
    }

    public DataComponentMap getTag() {
        if (this.isItem()) {
            return this.stack.getComponents() == null ? EMPTY : this.stack.getComponents();
        }
        return EMPTY;
    }

    public void addCount(int amount) {
        this.addCount(amount, false);
    }

    public void addCount(int amount, boolean ignoreFluid) {
        if (ignoreFluid) {
            if (!stack.isEmpty()) {
                stack.setCount(safeAmount(stack.getCount() + amount));
            } else if (!fluidStack.isEmpty()) {
                fluidStack.setAmount(safeAmount(fluidStack.getAmount() + amount));
            }
        } else {
            if (!fluidStack.isEmpty()) {
                fluidStack.setAmount(safeAmount(fluidStack.getAmount() + amount));
            } else if (!stack.isEmpty()) {
                stack.setCount(safeAmount(stack.getCount() + amount));
            }
        }
    }

    public int getAmount() {
        return this.getAmount(false);
    }

    public void setAmount(int amount) {
        amount = safeAmount(amount);

        if (this.isItem()) {
            this.stack.setCount(amount);
        } else if (this.isFluid()) {
            this.fluidStack.setAmount(amount);
        }
    }

    public int getAmount(boolean ignoreFluid) {
        if (ignoreFluid) {
            if (stack.isEmpty()) {
                return fluidStack.isEmpty() ? 1 : safeAmount(fluidStack.getAmount());
            }
            return safeAmount(stack.getCount());
        } else {
            if (fluidStack.isEmpty()) {
                return stack.isEmpty() ? 1 : safeAmount(stack.getCount());
            }
            return safeAmount(fluidStack.getAmount());
        }
    }

    public CompoundTag writeToNBT(HolderLookup.Provider registryAccess) {
        CompoundTag nbt = new CompoundTag();

        int amount = safeAmount(this.getAmount());
        nbt.putInt("count", amount);

        if (!stack.isEmpty()) {
            ItemStack stackForSave = copyForNbt(stack);

            if (!stackForSave.isEmpty()) {
                nbt.put("Item", stackForSave.save(registryAccess));
            }
        }

        if (!fluidStack.isEmpty()) {
            FluidStack fluidForSave = fluidStack.copy();
            fluidForSave.setAmount(amount);
            nbt.put("Fluid", fluidForSave.save(registryAccess));
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
            if (this.isItem()) {
                key = BuiltInRegistries.ITEM.getKey(this.stack.getItem()).toString();
            } else if (this.isFluid()) {
                key = BuiltInRegistries.FLUID.getKey(this.getFluidStack().getFluid()).toString();
            }
        }
        return key;
    }

    public SameStack copy() {
        SameStack sameStack;

        if (this.isItem()) {
            sameStack = new SameStack(this.stack.copy());
        } else if (this.isFluid()) {
            sameStack = new SameStack(this.fluidStack.copy());
        } else {
            sameStack = new SameStack();
        }

        sameStack.key = this.key;
        return sameStack;
    }

    public SameStack copyWithFluid() {
        SameStack sameStack = new SameStack(this.stack.copy());
        sameStack.setFluidStack(this.fluidStack.copy());
        sameStack.key = this.key;
        return sameStack;
    }

    public void setAmount(int amount, boolean ignoreFluid) {
        amount = safeAmount(amount);

        if (ignoreFluid) {
            this.setAmount(amount);
        } else {
            if (this.isFluid()) {
                this.fluidStack.setAmount(amount);
            } else if (this.isItem()) {
                this.stack.setCount(amount);
            }
        }
    }

    public boolean isEmpty() {
        return !this.isItem() && !this.isFluid();
    }
}