package com.denfop.api.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IWrenchable {

    EnumFacing getFacing(World var1, BlockPos var2);

    default boolean canSetFacing(World world, BlockPos pos, EnumFacing newDirection, EntityPlayer player) {
        return true;
    }

    boolean setFacing(World var1, BlockPos var2, EnumFacing var3, EntityPlayer var4);

    boolean wrenchCanRemove(World var1, BlockPos var2, EntityPlayer var3);

    List<ItemStack> getWrenchDrops(World var1, BlockPos var2, IBlockState var3, TileEntity var4, EntityPlayer var5, int var6);

    void wrenchBreak(World world, BlockPos pos);

}
