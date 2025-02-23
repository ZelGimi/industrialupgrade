package com.denfop.tiles.mechanism.steamturbine.exchanger;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;

public class TileEntityPerSteamTurbineExchanger extends TileEntityBaseSteamTurbineExchanger{

    public TileEntityPerSteamTurbineExchanger() {
        super(3);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_per_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }
}
