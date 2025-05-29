package com.denfop.tiles.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTank;
import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvTank extends TileEntityLiquedTank {

    public TileEntityAdvTank(BlockPos pos, BlockState state) {
        super(160, BlockTank.adv_tank, pos, state);
    }


    public IMultiTileBlock getTeBlock() {
        return BlockTank.adv_tank;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tank.getBlock(getTeBlock().getId());
    }

}
