package com.denfop.tiles.mechanism.generator.things.matter;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileMultiMatter;

public class TilePhotonicMatter extends TileMultiMatter {


    public TilePhotonicMatter() {
        super(600000F, 16, 512000000);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_gen_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

}
