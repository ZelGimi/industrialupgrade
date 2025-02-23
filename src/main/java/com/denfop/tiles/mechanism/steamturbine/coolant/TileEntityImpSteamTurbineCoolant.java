package com.denfop.tiles.mechanism.steamturbine.coolant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;

public class TileEntityImpSteamTurbineCoolant extends TileEntityBaseSteamTurbineCoolant{

    public TileEntityImpSteamTurbineCoolant() {
        super(2);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_imp_coolant;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }
}
