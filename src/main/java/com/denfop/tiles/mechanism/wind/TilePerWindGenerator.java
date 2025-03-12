package com.denfop.tiles.mechanism.wind;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;

public class TilePerWindGenerator extends TileWindGenerator {


    public TilePerWindGenerator() {
        super(EnumLevelGenerators.FOUR);
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.per_wind_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
