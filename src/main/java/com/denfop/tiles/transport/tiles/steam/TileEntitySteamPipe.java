package com.denfop.tiles.transport.tiles.steam;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamPipe;
import com.denfop.tiles.transport.types.SteamType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySteamPipe extends com.denfop.tiles.transport.tiles.TileEntitySteamPipe {
    public TileEntitySteamPipe(BlockPos pos, BlockState state) {
        super(SteamType.spipe, BlockSteamPipe.steam, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamPipe.steam;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steamPipe.getBlock(getTeBlock().getId());
    }
}
