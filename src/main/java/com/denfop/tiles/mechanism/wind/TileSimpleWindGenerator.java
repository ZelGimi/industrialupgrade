package com.denfop.tiles.mechanism.wind;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;

public class TileSimpleWindGenerator extends TileWindGenerator {



    public TileSimpleWindGenerator() {
        super(EnumLevelGenerators.ONE);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.simple_wind_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
