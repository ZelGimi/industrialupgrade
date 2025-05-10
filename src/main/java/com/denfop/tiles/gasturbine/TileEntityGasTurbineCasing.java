package com.denfop.tiles.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasTurbine;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityGasTurbineCasing extends TileEntityMultiBlockElement implements ICasing {

    public TileEntityGasTurbineCasing(BlockPos pos, BlockState state) {
        super(BlockGasTurbine.gas_turbine_casing, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasTurbine.gas_turbine_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gasTurbine.getBlock(getTeBlock());
    }

}
