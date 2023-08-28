package com.denfop.tiles.mechanism.quarry;


import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.EnumTypeStyle;

public class TilePerQuantumQuarry extends TileBaseQuantumQuarry {

    public TilePerQuantumQuarry() {
        super(1);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.per_quantum_quarry;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

}
