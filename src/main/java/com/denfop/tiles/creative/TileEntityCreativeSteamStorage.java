package com.denfop.tiles.creative;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCreativeBlocks;
import com.denfop.tiles.mechanism.steam.TileSteamStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCreativeSteamStorage extends TileSteamStorage {
    public TileEntityCreativeSteamStorage(BlockPos pos, BlockState state) {
        super(BlockCreativeBlocks.creative_steam_storage, pos, state, 1024);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.creativeBlock.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCreativeBlocks.creative_steam_storage;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.steam.addEnergy(10240000);
    }


}
