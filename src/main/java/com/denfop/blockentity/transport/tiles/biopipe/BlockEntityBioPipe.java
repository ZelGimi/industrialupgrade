package com.denfop.blockentity.transport.tiles.biopipe;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.types.BioType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBioPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityBioPipe extends com.denfop.blockentity.transport.tiles.BlockEntityBioPipe {
    public BlockEntityBioPipe(BlockPos pos, BlockState state) {
        super(BioType.bpipe, BlockBioPipeEntity.biofuel, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBioPipeEntity.biofuel;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.biopipes.getBlock(getTeBlock().getId());
    }
}
