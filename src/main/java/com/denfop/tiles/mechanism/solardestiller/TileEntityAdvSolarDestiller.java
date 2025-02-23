package com.denfop.tiles.mechanism.solardestiller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityAdvSolarDestiller extends TileEntityBaseSolarDestiller {

    public TileEntityAdvSolarDestiller() {
        super(EnumTypeStyle.ADVANCED);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.adv_solar_destiller;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
