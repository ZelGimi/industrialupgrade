package com.denfop.tiles.mechanism.steamturbine.coolant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvSteamTurbineCoolant extends TileEntityBaseSteamTurbineCoolant {

    public TileEntityAdvSteamTurbineCoolant(BlockPos pos, BlockState state) {
        super(1,BlockSteamTurbine.steam_turbine_adv_coolant,pos,state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_adv_coolant;
    }
    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
    }

}
