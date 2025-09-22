package com.denfop.tiles.mechanism.water;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileSimpleWaterGenerator extends TileBaseWaterGenerator {


    public TileSimpleWaterGenerator() {
        super(EnumLevelGenerators.ONE);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.simple_water_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
