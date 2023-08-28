package com.denfop.tiles.mechanism.solardestiller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityPerSolarDestiller extends TileEntityBaseSolarDestiller {

    public TileEntityPerSolarDestiller() {
        super(EnumTypeStyle.PERFECT);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.per_solar_destiller;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
