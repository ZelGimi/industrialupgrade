package com.denfop.tiles.mechanism.generator.things.matter;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileMultiMatter;

public class TileUltimateMatter extends TileMultiMatter {

    public TileUltimateMatter() {
        super(700000F, 16, 256000000);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.per_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

}
