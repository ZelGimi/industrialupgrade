package com.denfop.tiles.mechanism.generator.things.matter;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileMultiMatter;

public class TileImprovedMatter extends TileMultiMatter {

    public TileImprovedMatter() {
        super(800000F, 14, 64000000);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.imp_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

}
