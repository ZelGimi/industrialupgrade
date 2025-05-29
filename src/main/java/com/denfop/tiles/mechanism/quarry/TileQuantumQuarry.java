package com.denfop.tiles.mechanism.quarry;


import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileQuantumQuarry extends TileBaseQuantumQuarry {

    public TileQuantumQuarry(BlockPos pos, BlockState state) {
        super(4, BlockBaseMachine.quantum_quarry, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.quantum_quarry;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }
}
