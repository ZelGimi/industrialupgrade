package com.denfop.blockentity.transport.tiles.solarium;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntitySCable;
import com.denfop.blockentity.transport.types.SEType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolariumCableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySolariumCable extends BlockEntitySCable {
    public BlockEntitySolariumCable(BlockPos pos, BlockState state) {
        super(SEType.scable, BlockSolariumCableEntity.solarium, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSolariumCableEntity.solarium;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.scable.getBlock(getTeBlock().getId());
    }
}
