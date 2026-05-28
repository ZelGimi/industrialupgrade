package com.denfop.ability;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class AxeTreeAbility implements IPlayerAbility {

    private static final int MAX_LOGS = 160;

    @Override
    public EnumPlayerAbility type() {
        return EnumPlayerAbility.AXE_TREE;
    }

    @Override
    public boolean supports(final ItemStack stack) {
        return AbilityToolHelper.isSupportedVanillaAxe(stack);
    }

    @Override
    public AbilityActivationResult activate(
            final ServerPlayer player,
            final ItemStack stack,
            final BlockPos origin
    ) {
        final List<BlockPos> targets = AbilityTreeSearch.collectTree(player, stack, origin, MAX_LOGS);
        if (targets.isEmpty()) {
            return AbilityActivationResult.fail(Component.translatable("iu.ability.tree.not_found"));
        }

        final int estimatedFood = AbilityToolHelper.calculateFoodCost(targets.size(), MAX_LOGS);
        if (!player.isCreative() && player.getFoodData().getFoodLevel() < estimatedFood) {
            return AbilityActivationResult.fail(Component.translatable("iu.ability.not_enough_food"));
        }

        final int broken = AbilityBlockBreaker.breakBlocks(player, stack, targets);
        if (broken <= 0) {
            return AbilityActivationResult.fail(Component.translatable("iu.ability.tree.not_chopped"));
        }

        return AbilityActivationResult.success(
                broken,
                AbilityToolHelper.calculateFoodCost(broken, MAX_LOGS),
                this.type().getDefaultCooldownTicks()
        );
    }
}
