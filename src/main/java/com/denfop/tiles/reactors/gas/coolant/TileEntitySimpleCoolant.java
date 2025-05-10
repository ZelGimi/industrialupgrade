package com.denfop.tiles.reactors.gas.coolant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySimpleCoolant extends TileEntityCoolant {

    public TileEntitySimpleCoolant(BlockPos pos, BlockState state) {
        super(0,BlockGasReactor.gas_coolant,pos,state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.gas_coolant;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
