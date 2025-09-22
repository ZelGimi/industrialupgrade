package com.denfop.tiles.mechanism.steamturbine.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;

public class TileEntityImpSteamTurbineTank extends TileEntityBaseSteamTurbineTank {

    public TileEntityImpSteamTurbineTank() {
        super(2);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_imp_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }

}
