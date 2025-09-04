package com.denfop.blockentity.creative;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.radiation_storage.BlockEntityRadiationStorage;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCreativeBlocksEntity;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCreativeRadiationStorage extends BlockEntityRadiationStorage {
    public BlockEntityCreativeRadiationStorage(BlockPos pos, BlockState state) {
        super(Math.pow(10, 24), EnumTypeStyle.PERFECT, BlockCreativeBlocksEntity.creative_radiation_storage, pos, state);
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
    public MultiBlockEntity getTeBlock() {
        return BlockCreativeBlocksEntity.creative_radiation_storage;
    }
}
