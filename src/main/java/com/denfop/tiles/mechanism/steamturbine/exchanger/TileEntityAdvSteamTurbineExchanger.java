package com.denfop.tiles.mechanism.steamturbine.exchanger;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;

public class TileEntityAdvSteamTurbineExchanger extends TileEntityBaseSteamTurbineExchanger{

    public TileEntityAdvSteamTurbineExchanger() {
        super(1);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_adv_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }
}
