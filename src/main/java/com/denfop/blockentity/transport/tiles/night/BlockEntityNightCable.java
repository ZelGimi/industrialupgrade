package com.denfop.blockentity.transport.tiles.night;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityNightPipe;
import com.denfop.blockentity.transport.types.NightType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockNightCableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityNightCable extends BlockEntityNightPipe {
    public BlockEntityNightCable(BlockPos pos, BlockState state) {
        super(NightType.npipe, BlockNightCableEntity.night, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockNightCableEntity.night;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.nightpipes.getBlock(getTeBlock().getId());
    }
}
