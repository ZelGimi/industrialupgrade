package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileAdvRedstoneGenerator extends TileBaseRedstoneGenerator {

    public TileAdvRedstoneGenerator() {
        super(2.2, 2);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.adv_redstone_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
