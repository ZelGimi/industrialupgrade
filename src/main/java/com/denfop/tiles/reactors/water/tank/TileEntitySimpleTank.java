package com.denfop.tiles.reactors.water.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.reactors.water.ITank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySimpleTank extends TileEntityMainTank implements ITank {

    public TileEntitySimpleTank(BlockPos pos, BlockState state) {
        super(10000,BlockWaterReactors.water_tank,pos,state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 0;
    }

}
