package com.denfop.tiles.windturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWindTurbine;
import com.denfop.componets.Energy;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityWindTurbineSocket extends TileEntityMultiBlockElement implements ISocket {

    private final Energy energy;

    public TileEntityWindTurbineSocket(BlockPos pos, BlockState state) {
        super(BlockWindTurbine.wind_turbine_socket, pos, state);
        this.energy = this.addComponent(Energy.asBasicSource(this, 2000000, 14));
    }


    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWindTurbine.wind_turbine_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.windTurbine.getBlock(getTeBlock());
    }

    @Override
    public Energy getEnergy() {
        return energy;
    }

}
