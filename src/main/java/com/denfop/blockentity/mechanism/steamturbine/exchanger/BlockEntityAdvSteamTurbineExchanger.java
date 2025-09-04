package com.denfop.blockentity.mechanism.steamturbine.exchanger;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvSteamTurbineExchanger extends BlockEntityBaseSteamTurbineExchanger {

    public BlockEntityAdvSteamTurbineExchanger(BlockPos pos, BlockState state) {
        super(1, BlockSteamTurbineEntity.steam_turbine_adv_exchanger, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamTurbineEntity.steam_turbine_adv_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
    }

}
