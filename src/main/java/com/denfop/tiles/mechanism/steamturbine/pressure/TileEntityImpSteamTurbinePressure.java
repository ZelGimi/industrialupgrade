package com.denfop.tiles.mechanism.steamturbine.pressure;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityImpSteamTurbinePressure extends TileEntityBaseSteamTurbinePressure {

    public TileEntityImpSteamTurbinePressure(BlockPos pos, BlockState state) {
        super(2, BlockSteamTurbine.steam_turbine_imp_pressure, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_imp_pressure;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
    }

}
