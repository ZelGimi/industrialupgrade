package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityGeoGenerator extends com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator {

    public TileEntityGeoGenerator() {
        super(10, 1, 1);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.geogenerator_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
