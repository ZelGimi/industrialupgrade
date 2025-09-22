package com.denfop.tiles.mechanism.steamturbine.pressure;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;

public class TileEntityAdvSteamTurbinePressure extends TileEntityBaseSteamTurbinePressure {

    public TileEntityAdvSteamTurbinePressure() {
        super(1);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_adv_pressure;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }

}
