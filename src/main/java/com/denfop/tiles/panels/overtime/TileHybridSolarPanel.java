package com.denfop.tiles.panels.overtime;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolarPanels;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;

public class TileHybridSolarPanel extends TileSolarPanel {


    public TileHybridSolarPanel() {
        super(EnumSolarPanels.HYBRID_SOLAR_PANEL);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockSolarPanels.hybrid_solar_paneliu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockpanel;
    }

}
