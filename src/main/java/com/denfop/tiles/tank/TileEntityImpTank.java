package com.denfop.tiles.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTank;
import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

public class TileEntityImpTank extends TileEntityLiquedTank {

    public TileEntityImpTank() {
        super(480);
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.tank, 1, 2);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTank.imp_tank;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tank;
    }

}
