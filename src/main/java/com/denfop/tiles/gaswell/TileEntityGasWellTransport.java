package com.denfop.tiles.gaswell;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityGasWellTransport extends TileEntityMultiBlockElement implements ITransport {


    public TileEntityGasWellTransport(BlockPos pos, BlockState state) {
        super( BlockGasWell.gas_well_transport, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasWell.gas_well_transport;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_well.getBlock(getTeBlock());
    }

}
