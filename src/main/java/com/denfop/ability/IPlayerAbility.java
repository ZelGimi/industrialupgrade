package com.denfop.ability;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public interface IPlayerAbility {

    EnumPlayerAbility type();

    boolean supports(ItemStack stack);

    AbilityActivationResult activate(ServerPlayer player, ItemStack stack, BlockPos origin);
}
