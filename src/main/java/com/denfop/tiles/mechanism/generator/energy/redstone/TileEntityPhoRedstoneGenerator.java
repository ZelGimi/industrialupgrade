package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityPhoRedstoneGenerator extends TileBaseRedstoneGenerator {


    public TileEntityPhoRedstoneGenerator() {
        super(6, 8);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_redstone_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

}
