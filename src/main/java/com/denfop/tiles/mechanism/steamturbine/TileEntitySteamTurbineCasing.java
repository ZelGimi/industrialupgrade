package com.denfop.tiles.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntitySteamTurbineCasing extends TileEntityMultiBlockElement implements ICasing {
public TileEntitySteamTurbineCasing(){}

    @Override
    public int getLevel() {
        return -1;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }

}
