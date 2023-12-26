package com.denfop.tiles.reactors.gas.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityPerController    extends TileEntityMainController {

    public TileEntityPerController() {
        super(InitMultiBlockSystem.perGasReactorMultiBlock, EnumGasReactors.P);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.per_gas_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
