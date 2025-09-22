package com.denfop.tiles.mechanism.steamturbine.exchanger;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;

public class TileEntityImpSteamTurbineExchanger extends TileEntityBaseSteamTurbineExchanger {

    public TileEntityImpSteamTurbineExchanger() {
        super(2);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_imp_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }

}
