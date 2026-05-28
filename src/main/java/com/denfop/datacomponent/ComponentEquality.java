package com.denfop.datacomponent;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Objects;

public final class ComponentEquality {

    private ComponentEquality() {
    }

    public static boolean sameItemStack(ItemStack a, ItemStack b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.isEmpty() && b.isEmpty()) {
            return true;
        }
        if (a.isEmpty() != b.isEmpty()) {
            return false;
        }
        return a.getCount() == b.getCount() && ItemStack.isSameItemSameComponents(a, b);
    }

    public static int itemStackHash(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        return 31 * ItemStack.hashItemAndComponents(stack) + stack.getCount();
    }

    public static boolean sameItemStackList(List<ItemStack> a, List<ItemStack> b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return ItemStack.listMatches(a, b);
    }

    public static int itemStackListHash(List<ItemStack> stacks) {
        if (stacks == null) {
            return 0;
        }
        return ItemStack.hashStackList(stacks);
    }

    public static boolean sameFluidStack(FluidStack a, FluidStack b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.isEmpty() && b.isEmpty()) {
            return true;
        }
        if (a.isEmpty() != b.isEmpty()) {
            return false;
        }

        return a.getAmount() == b.getAmount()
                && a.getFluid() == b.getFluid()
                && Objects.equals(a.getComponents(), b.getComponents());
    }

    public static int fluidStackHash(FluidStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        return Objects.hash(
                stack.getFluid(),
                stack.getAmount(),
                stack.getComponents()
        );
    }

    public static boolean sameFluidStackList(List<FluidStack> a, List<FluidStack> b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null || a.size() != b.size()) {
            return false;
        }

        for (int i = 0; i < a.size(); i++) {
            if (!sameFluidStack(a.get(i), b.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static int fluidStackListHash(List<FluidStack> stacks) {
        if (stacks == null) {
            return 0;
        }

        int result = 1;
        for (FluidStack stack : stacks) {
            result = 31 * result + fluidStackHash(stack);
        }
        return result;
    }
}