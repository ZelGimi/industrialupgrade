package com.denfop.tiles.panels.overtime;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolarPanels;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;

public class TileHadronSolarPanel extends TileSolarPanel {

    public TileHadronSolarPanel() {
        super(EnumSolarPanels.HADRON_SOLAR_PANEL);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockSolarPanels.hadron_solar_panel;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockpanel;
    }

}
