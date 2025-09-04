package com.denfop.blockentity.mechanism.steamturbine.tank;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleSteamTurbineTank extends BlockEntityBaseSteamTurbineTank {

    public BlockEntitySimpleSteamTurbineTank(BlockPos pos, BlockState state) {
        super(0, BlockSteamTurbineEntity.steam_turbine_tank, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamTurbineEntity.steam_turbine_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
    }

}
