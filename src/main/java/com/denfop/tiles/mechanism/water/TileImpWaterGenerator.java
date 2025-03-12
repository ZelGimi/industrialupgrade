package com.denfop.tiles.mechanism.water;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;

public class TileImpWaterGenerator extends TileBaseWaterGenerator {



    public TileImpWaterGenerator() {
        super(EnumLevelGenerators.THREE);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.imp_water_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
