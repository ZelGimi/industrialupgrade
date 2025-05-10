package com.denfop.tiles.mechanism.steamboiler;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamBoiler;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySteamCasingBoiler extends TileEntityMultiBlockElement implements ICasing {

    public TileEntitySteamCasingBoiler(BlockPos pos, BlockState state) {
        super(BlockSteamBoiler.steam_boiler_casing, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamBoiler.steam_boiler_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_boiler.getBlock(getTeBlock());
    }

}
