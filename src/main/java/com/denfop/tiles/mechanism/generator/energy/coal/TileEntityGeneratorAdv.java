package com.denfop.tiles.mechanism.generator.energy.coal;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityGeneratorAdv extends TileEntityAdvGenerator {

    public TileEntityGeneratorAdv() {
        super(2.2, 8000, 2);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.adv_gen;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

}
