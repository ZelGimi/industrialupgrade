package com.denfop.tiles.mechanism.steamturbine.coolant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;

public class TileEntityPerSteamTurbineCoolant extends TileEntityBaseSteamTurbineCoolant {

    public TileEntityPerSteamTurbineCoolant() {
        super(3);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_per_coolant;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }

}
