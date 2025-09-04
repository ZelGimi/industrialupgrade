package com.denfop.blockentity.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySteamTurbineCasing extends BlockEntityMultiBlockElement implements ICasing {


    public BlockEntitySteamTurbineCasing(BlockPos pos, BlockState state) {
        super(BlockSteamTurbineEntity.steam_turbine_casing, pos, state);
    }

    @Override
    public int getBlockLevel() {
        return -1;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamTurbineEntity.steam_turbine_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
    }

}
