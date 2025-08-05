package com.denfop.tiles.creative;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCreativeBlocks;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.solarium_storage.TileEntitySolariumStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCreativeSolariumStorage extends TileEntitySolariumStorage {
    public TileEntityCreativeSolariumStorage(BlockPos pos, BlockState state) {
        super(Math.pow(10, 24), EnumTypeStyle.PERFECT, BlockCreativeBlocks.creative_solarium_storage, pos, state);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.creativeBlock.getBlock(getTeBlock());
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.se.storage = this.se.capacity;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCreativeBlocks.creative_solarium_storage;
    }
}
