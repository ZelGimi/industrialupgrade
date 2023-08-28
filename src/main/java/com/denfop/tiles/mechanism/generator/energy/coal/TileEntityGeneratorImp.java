package com.denfop.tiles.mechanism.generator.energy.coal;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityGeneratorImp extends TileEntityAdvGenerator {

    public TileEntityGeneratorImp() {
        super(3.4, 16000, 3);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.imp_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

}
