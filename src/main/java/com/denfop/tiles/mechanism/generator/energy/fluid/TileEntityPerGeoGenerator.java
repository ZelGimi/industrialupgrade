package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator;

public class TileEntityPerGeoGenerator extends TileEntityGeoGenerator {

    public TileEntityPerGeoGenerator() {
        super(32, 4.6, 4);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.per_geo;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine;
    }

}
