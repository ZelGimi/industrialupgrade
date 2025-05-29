package com.denfop.tiles.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTank;
import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityTank extends TileEntityLiquedTank {

    public TileEntityTank(BlockPos pos, BlockState state) {
        super(40, BlockTank.tank_iu, pos, state);
    }


    public IMultiTileBlock getTeBlock() {
        return BlockTank.tank_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tank.getBlock(getTeBlock().getId());
    }

}
