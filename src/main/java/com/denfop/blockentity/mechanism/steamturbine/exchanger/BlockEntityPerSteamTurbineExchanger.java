package com.denfop.blockentity.mechanism.steamturbine.exchanger;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerSteamTurbineExchanger extends BlockEntityBaseSteamTurbineExchanger {

    public BlockEntityPerSteamTurbineExchanger(BlockPos pos, BlockState state) {
        super(3, BlockSteamTurbineEntity.steam_turbine_per_exchanger, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamTurbineEntity.steam_turbine_per_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
    }

}
