package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator;

public class TileEntityAdvGeoGenerator extends TileEntityGeoGenerator {

    public TileEntityAdvGeoGenerator() {
        super(16, 2.2, 2);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.adv_geo;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine;
    }

}
