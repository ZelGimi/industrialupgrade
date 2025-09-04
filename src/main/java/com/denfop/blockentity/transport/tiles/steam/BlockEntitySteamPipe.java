package com.denfop.blockentity.transport.tiles.steam;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.types.SteamType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySteamPipe extends com.denfop.blockentity.transport.tiles.BlockEntitySteamPipe {
    public BlockEntitySteamPipe(BlockPos pos, BlockState state) {
        super(SteamType.spipe, BlockSteamPipeEntity.steam, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamPipeEntity.steam;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steamPipe.getBlock(getTeBlock().getId());
    }
}
