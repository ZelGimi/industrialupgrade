package com.denfop.tiles.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTank;
import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

public class TileEntityTank extends TileEntityLiquedTank {

    public TileEntityTank() {
        super(40, "tank");
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.tank, 1, 0);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTank.tank_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tank;
    }

}
