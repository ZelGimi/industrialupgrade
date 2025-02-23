package com.denfop.api.upgrade;

import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;

public interface ILevelInstruments {

    default int getLevel(ItemStack stack) {
        return ModUtils.nbt(stack).getInteger("level");
    }

    ;

    default int getExperience(ItemStack stack) {
        return ModUtils.nbt(stack).getInteger("experience");
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
            ModUtils.nbt(stack).setInteger("level", getLevel(stack) + 1);
            UpgradeSystem.system.updateLevel(stack);
        }
        ModUtils.nbt(stack).setInteger("experience", exp);
    }

}
