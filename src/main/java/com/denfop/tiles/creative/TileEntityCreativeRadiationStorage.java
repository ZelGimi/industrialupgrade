package com.denfop.tiles.creative;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCreativeBlocks;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityQuantumStorage;
import com.denfop.tiles.mechanism.radiation_storage.TileEntityRadiationStorage;
import com.denfop.tiles.mechanism.solarium_storage.TileEntitySolariumStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCreativeRadiationStorage extends TileEntityRadiationStorage {
    public TileEntityCreativeRadiationStorage(BlockPos pos, BlockState state) {
        super(Math.pow(10,24), EnumTypeStyle.PERFECT, BlockCreativeBlocks.creative_radiation_storage, pos, state);
    }
    @Override
    public BlockTileEntity getBlock() {
        return IUItem.creativeBlock.getBlock(getTeBlock());
    }
    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.radiation.buffer.storage = this.radiation.buffer.capacity;
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return  BlockCreativeBlocks.creative_radiation_storage;
    }
}
