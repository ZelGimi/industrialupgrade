package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator;

public class TileEntityImpGeoGenerator extends TileEntityGeoGenerator {

    public TileEntityImpGeoGenerator() {
        super(20, 3.4, 3);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.imp_geo;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine;
    }

}
