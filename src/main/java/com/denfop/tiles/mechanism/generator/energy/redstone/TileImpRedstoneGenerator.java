package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileImpRedstoneGenerator extends TileBaseRedstoneGenerator {

    public TileImpRedstoneGenerator() {
        super(3.4, 4);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.imp_redstone_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
