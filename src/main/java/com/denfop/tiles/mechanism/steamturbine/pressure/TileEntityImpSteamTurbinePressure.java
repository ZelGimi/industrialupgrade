package com.denfop.tiles.mechanism.steamturbine.pressure;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;

public class TileEntityImpSteamTurbinePressure extends TileEntityBaseSteamTurbinePressure {

    public TileEntityImpSteamTurbinePressure() {
        super(2);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_imp_pressure;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }

}
