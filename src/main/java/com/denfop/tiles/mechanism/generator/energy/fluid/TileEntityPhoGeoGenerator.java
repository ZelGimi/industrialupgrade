package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator;

public class TileEntityPhoGeoGenerator extends TileEntityGeoGenerator {


    public TileEntityPhoGeoGenerator() {
        super(64, 6, 8);

    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_geogenerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

}
