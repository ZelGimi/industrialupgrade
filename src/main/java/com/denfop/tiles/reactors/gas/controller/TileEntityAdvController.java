package com.denfop.tiles.reactors.gas.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvController extends TileEntityMainController {

    public TileEntityAdvController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.advGasReactorMultiBlock, EnumGasReactors.A,BlockGasReactor.adv_gas_controller,pos,state);
    }


    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.adv_gas_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
