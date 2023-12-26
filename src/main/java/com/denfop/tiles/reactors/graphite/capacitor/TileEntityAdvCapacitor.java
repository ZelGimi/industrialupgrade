package com.denfop.tiles.reactors.graphite.capacitor;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAdvCapacitor extends TileEntityCapacitor {

    public TileEntityAdvCapacitor() {
        super(1);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_adv_capacitor;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

}
