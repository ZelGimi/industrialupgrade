package com.denfop.tiles.windturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWindTurbine;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityWindTurbineStabilizer extends TileEntityMultiBlockElement implements IStabilizer {

    public TileEntityWindTurbineStabilizer(BlockPos pos, BlockState state) {
        super(BlockWindTurbine.wind_turbine_stabilizer, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWindTurbine.wind_turbine_stabilizer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.windTurbine.getBlock(getTeBlock());
    }

}
