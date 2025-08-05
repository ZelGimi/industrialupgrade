package com.denfop.tiles.reactors.graphite.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvController extends TileEntityMainController {

    public TileEntityAdvController(BlockPos pos, BlockState state
    ) {
        super(InitMultiBlockSystem.advGraphiteReactorMultiBlock, EnumGraphiteReactors.A, BlocksGraphiteReactors.graphite_adv_controller, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_adv_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor.getBlock(getTeBlock());
    }

}
