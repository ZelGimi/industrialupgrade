package com.denfop.tiles.mechanism.generator.things.matter;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileMultiMatter;


public class TileAdvancedMatter extends TileMultiMatter {

    public TileAdvancedMatter() {
        super(900000F, 12, 8000000);
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.adv_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

}
