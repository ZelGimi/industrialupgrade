package com.denfop.tiles.mechanism.steamturbine.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;

public class TileEntityPerSteamTurbineTank extends TileEntityBaseSteamTurbineTank{

    public TileEntityPerSteamTurbineTank() {
        super(3);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_per_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }
}
