package com.denfop.tiles.mechanism.steamturbine.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;

public class TileEntitySimpleSteamTurbineController extends TileEntityBaseSteamTurbineController{

    public TileEntitySimpleSteamTurbineController() {
        super(0);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }
}
