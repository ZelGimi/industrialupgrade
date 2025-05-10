package com.denfop.tiles.reactors.gas.recirculation_pump;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvReCirculationPump extends TileEntityBaseReCirculationPump {

    public TileEntityAdvReCirculationPump(BlockPos pos, BlockState state) {
        super(1,BlockGasReactor.adv_gas_recirculation_pump,pos,state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.adv_gas_recirculation_pump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
