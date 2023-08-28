package com.denfop.tiles.mechanism.quarry;


import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.EnumTypeStyle;

public class TileImpQuantumQuarry extends TileBaseQuantumQuarry {

    public TileImpQuantumQuarry() {
        super(2);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.imp_quantum_quarry;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

}
