package com.denfop.blockentity.transport.tiles.amperecable;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.types.AmpereType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAmpereCableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAmpereCable extends com.denfop.blockentity.transport.tiles.BlockEntityAmpereCable {
    public BlockEntityAmpereCable(BlockPos pos, BlockState state) {
        super(AmpereType.acable, BlockAmpereCableEntity.ampere, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockAmpereCableEntity.ampere;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.amperepipes.getBlock(getTeBlock().getId());
    }
}
