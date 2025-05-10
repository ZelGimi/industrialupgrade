package com.denfop.tiles.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.componets.Energy;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySteamTurbineSocket extends TileEntityMultiBlockElement implements ISocket {

    Energy energy;

    public TileEntitySteamTurbineSocket(BlockPos pos, BlockState state) {
        super(BlockSteamTurbine.steam_turbine_socket, pos, state);
        energy = this.addComponent(Energy.asBasicSource(this, 200000, 14));
    }


    @Override
    public Energy getEnergy() {
        return energy;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_socket;
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
