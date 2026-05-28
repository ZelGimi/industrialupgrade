package com.denfop.ability;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class PickaxeVeinAbility implements IPlayerAbility {

    private static final int MAX_BLOCKS = 120;
    private static final int MAX_HORIZONTAL_RADIUS = 12;
    private static final int MAX_VERTICAL_RADIUS = 12;

    @Override
    public EnumPlayerAbility type() {
        return EnumPlayerAbility.PICKAXE_VEIN;
    }

    @Override
    public boolean supports(final ItemStack stack) {
        return AbilityToolHelper.isSupportedVanillaPickaxe(stack);
    }

    @Override
    public AbilityActivationResult activate(
            final ServerPlayer player,
            final ItemStack stack,
            final BlockPos origin
    ) {
        final List<BlockPos> targets = AbilityVeinSearch.collectVein(
                player,
                stack,
                origin,
                MAX_BLOCKS,
                MAX_HORIZONTAL_RADIUS,
                MAX_VERTICAL_RADIUS
        );

        if (targets.isEmpty()) {
            return AbilityActivationResult.fail(Component.translatable("iu.ability.vein.not_found"));
        }

        final int estimatedFood = AbilityToolHelper.calculateFoodCost(targets.size(), MAX_BLOCKS);
        if (!player.isCreative() && player.getFoodData().getFoodLevel() < estimatedFood) {
            return AbilityActivationResult.fail(Component.translatable("iu.ability.not_enough_food"));
        }

        final int broken = AbilityBlockBreaker.breakBlocks(player, stack, targets);
        if (broken <= 0) {
            return AbilityActivationResult.fail(Component.translatable("iu.ability.vein.none_mined"));
        }

        return AbilityActivationResult.success(
                broken,
                AbilityToolHelper.calculateFoodCost(broken, MAX_BLOCKS),
                this.type().getDefaultCooldownTicks()
        );
    }
}
