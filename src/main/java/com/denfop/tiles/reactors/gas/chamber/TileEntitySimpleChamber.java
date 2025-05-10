package com.denfop.tiles.reactors.gas.chamber;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IChamber;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySimpleChamber extends TileEntityMultiBlockElement implements IChamber {

    public TileEntitySimpleChamber(BlockPos pos, BlockState state) {
        super(BlockGasReactor.gas_chamber, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.gas_chamber;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 0;
    }

}
