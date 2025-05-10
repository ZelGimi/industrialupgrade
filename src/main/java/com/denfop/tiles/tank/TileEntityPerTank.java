package com.denfop.tiles.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTank;
import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityPerTank extends TileEntityLiquedTank {

    public TileEntityPerTank(BlockPos pos, BlockState state) {
        super(2560, BlockTank.per_tank, pos, state);
    }


    public IMultiTileBlock getTeBlock() {
        return BlockTank.per_tank;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tank.getBlock(getTeBlock().getId());
    }

}
