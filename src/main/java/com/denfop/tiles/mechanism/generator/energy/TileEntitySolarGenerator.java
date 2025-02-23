package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;

public class TileEntitySolarGenerator extends TileSolarPanel {


    public TileEntitySolarGenerator() {
        super(EnumSolarPanels.SOLAR_PANEL_DEFAULT);
    }


    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.solar_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }


}
