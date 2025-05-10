package com.denfop.tiles.reactors.heat.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityImpController extends TileEntityMainController {

    public TileEntityImpController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.impHeatReactorMultiBlock, EnumHeatReactors.I,BlockHeatReactor.heat_imp_controller,pos,state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_imp_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
