package com.denfop.blockentity.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbineEntity;
import com.denfop.componets.Energy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySteamTurbineSocket extends BlockEntityMultiBlockElement implements ISocket {

    Energy energy;

    public BlockEntitySteamTurbineSocket(BlockPos pos, BlockState state) {
        super(BlockSteamTurbineEntity.steam_turbine_socket, pos, state);
        energy = this.addComponent(Energy.asBasicSource(this, 200000, 14));
    }


    @Override
    public Energy getEnergy() {
        return energy;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamTurbineEntity.steam_turbine_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return -1;
    }

}
