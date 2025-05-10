package com.denfop.tiles.mechanism.steamturbine.exchanger;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvSteamTurbineExchanger extends TileEntityBaseSteamTurbineExchanger {

    public TileEntityAdvSteamTurbineExchanger(BlockPos pos, BlockState state) {
        super(1,BlockSteamTurbine.steam_turbine_adv_exchanger,pos,state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_adv_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
    }

}
