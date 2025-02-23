package com.denfop.tiles.mechanism.quarry;


import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.EnumTypeStyle;

public class TileAdvQuantumQuarry extends TileBaseQuantumQuarry {

    public TileAdvQuantumQuarry() {
        super(3);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.adv_quantum_quarry;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

}
