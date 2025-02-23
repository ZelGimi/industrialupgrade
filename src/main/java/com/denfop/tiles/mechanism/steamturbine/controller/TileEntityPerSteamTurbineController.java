package com.denfop.tiles.mechanism.steamturbine.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;

public class TileEntityPerSteamTurbineController extends TileEntityBaseSteamTurbineController{

    public TileEntityPerSteamTurbineController() {
        super(3);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_per_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }
}
