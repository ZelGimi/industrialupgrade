package com.denfop.tiles.mechanism.steamboiler;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamBoiler;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntitySteamCasingBoiler extends TileEntityMultiBlockElement implements ICasing{

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamBoiler.steam_boiler_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_boiler;
    }
}
