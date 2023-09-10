package com.denfop.tiles.mechanism.generator.energy.coal;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityGeneratorPer extends TileEntityAdvGenerator {

    public TileEntityGeneratorPer() {
        super(4.6, 32000, 4);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.per_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

}
