package com.denfop.api.upgrade;

import com.denfop.datacomponent.DataComponentsInit;
import net.minecraft.world.item.ItemStack;

public interface ILevelInstruments {

    default int getLevel(ItemStack stack) {
        return stack.getOrDefault(DataComponentsInit.LEVEL, 0);
    }

    ;

    default int getExperience(ItemStack stack) {
        return stack.getOrDefault(DataComponentsInit.EXPERIENCE, 0);
    }

    default int getMaxLevel(ItemStack stack) {
        int level = getLevel(stack);
        if (level >= 30) {
            return Integer.MAX_VALUE;
        }
        return (int) (Math.pow(2, Math.log(Math.exp(Math.log(level + 1)))) * (150 * Math.log((Math.pow(8, level + 1)))));
    }

    default void addExperience(ItemStack stack, int experience) {
        int exp = getExperience(stack) + experience;
        final int maxLevel = getMaxLevel(stack);
        if (maxLevel == Integer.MAX_VALUE) {
            return;
        }
        if (maxLevel < exp) {
            exp -= maxLevel;
            stack.set(DataComponentsInit.LEVEL, getLevel(stack) + 1);
            UpgradeSystem.system.updateLevel(stack);
        }
        stack.set(DataComponentsInit.EXPERIENCE, exp);
    }

}
