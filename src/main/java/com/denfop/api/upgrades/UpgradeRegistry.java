package com.denfop.api.upgrades;


import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpgradeRegistry {

    private static final List<ItemStack> upgrades = new ArrayList<>();

    public UpgradeRegistry() {
    }

    public static ItemStack register(ItemStack stack) {
        if (!(stack.getItem() instanceof IUpgradeItem)) {
            throw new IllegalArgumentException("The stack must represent an IUpgradeItem.");
        } else {
            upgrades.add(stack);
            return stack;
        }
    }

    public static Iterable<ItemStack> getUpgrades() {
        return Collections.unmodifiableCollection(upgrades);
    }

}
