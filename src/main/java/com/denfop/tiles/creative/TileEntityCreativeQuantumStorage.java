package com.denfop.tiles.creative;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCreativeBlocks;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityQuantumStorage;
import com.denfop.tiles.mechanism.solarium_storage.TileEntitySolariumStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCreativeQuantumStorage extends TileEntityQuantumStorage {
    public TileEntityCreativeQuantumStorage(BlockPos pos, BlockState state) {
        super(Math.pow(10,24), EnumTypeStyle.PERFECT, BlockCreativeBlocks.creative_quantum_storage, pos, state);
    }
    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.qe.buffer.storage = this.qe.buffer.capacity;
    }
    @Override
    public BlockTileEntity getBlock() {
        return IUItem.creativeBlock.getBlock(getTeBlock());
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return  BlockCreativeBlocks.creative_quantum_storage;
    }
}
