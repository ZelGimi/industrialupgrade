package com.denfop.tiles.transport.tiles.quantum;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockQuantumCable;
import com.denfop.tiles.transport.tiles.TileEntityQCable;
import com.denfop.tiles.transport.types.QEType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityQuantumCable extends TileEntityQCable {
    public TileEntityQuantumCable(BlockPos pos, BlockState state) {
        super(QEType.qcable, BlockQuantumCable.quantum, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockQuantumCable.quantum;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.qcable.getBlock(getTeBlock().getId());
    }
}
