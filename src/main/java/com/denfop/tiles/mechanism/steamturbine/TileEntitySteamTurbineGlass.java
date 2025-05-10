package com.denfop.tiles.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySteamTurbineGlass extends TileEntityMultiBlockElement implements IGlass {


    public TileEntitySteamTurbineGlass(BlockPos pos, BlockState state) {
        super(BlockSteamTurbine.steam_turbine_casing_glass, pos, state);
    }

    @Override
    public int getBlockLevel() {
        return -1;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_casing_glass;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
    }




}
