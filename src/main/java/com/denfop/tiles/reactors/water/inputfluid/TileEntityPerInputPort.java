package com.denfop.tiles.reactors.water.inputfluid;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityPerInputPort extends TileEntityInputFluid {

    public TileEntityPerInputPort(BlockPos pos, BlockState state) {
        super(BlockWaterReactors.water_per_input, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_per_input;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 3;
    }

}
