package com.denfop.tiles.mechanism.quantum_storage;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityImpQuantumStorage extends TileEntityQuantumStorage {

    public TileEntityImpQuantumStorage(BlockPos pos, BlockState state) {
        super(16000000, EnumTypeStyle.IMPROVED,BlockBaseMachine3.imp_quantum_storage,pos,state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.imp_quantum_storage;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
