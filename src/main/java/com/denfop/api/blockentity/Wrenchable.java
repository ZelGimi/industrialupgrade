package com.denfop.api.blockentity;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface Wrenchable {

    Direction getFacing(Level var1, BlockPos var2);

    boolean setFacing(Level var1, BlockPos var2, Direction var3, Player var4);

    boolean wrenchCanRemove(Level var1, BlockPos var2, Player var3);

    List<ItemStack> getWrenchDrops(Level var1, BlockPos var2, BlockState var3, BlockEntity var4, Player var5, int var6);

    void wrenchBreak(Level world, BlockPos pos);

}
