package com.denfop.ability;

import com.denfop.items.energy.instruments.ItemEnergyInstruments;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;

public final class AbilityToolHelper {

    private AbilityToolHelper() {
    }

    public static boolean isSupportedVanillaPickaxe(final ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (stack.getItem() instanceof ItemEnergyInstruments) {
            return false;
        }

        return stack.canPerformAction(ItemAbilities.PICKAXE_DIG);
    }

    public static boolean isSupportedVanillaAxe(final ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (stack.getItem() instanceof ItemEnergyInstruments) {
            return false;
        }

        return stack.canPerformAction(ItemAbilities.AXE_DIG);
    }

    public static IPlayerAbility resolveAbility(final ItemStack stack) {
        if (isSupportedVanillaPickaxe(stack)) {
            return new PickaxeVeinAbility();
        }

        if (isSupportedVanillaAxe(stack)) {
            return new AxeTreeAbility();
        }

        return null;
    }

    public static int calculateFoodCost(final int brokenBlocks, final int maxBlocksForScale) {
        if (brokenBlocks <= 0) {
            return 0;
        }

        if (maxBlocksForScale <= 1) {
            return 1;
        }

        final float progress = Mth.clamp((brokenBlocks - 1) / (float) (maxBlocksForScale - 1), 0.0F, 1.0F);
        return 1 + Mth.floor(progress * 4.0F);
    }
}
