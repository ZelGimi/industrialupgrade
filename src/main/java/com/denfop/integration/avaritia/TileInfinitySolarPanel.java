package com.denfop.integration.avaritia;


import com.denfop.Config;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;

public class TileInfinitySolarPanel extends TileSolarPanel {

    public TileInfinitySolarPanel() {
        super(EnumSolarPanels.INFINITY_SOLAR_PANEL);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockAvaritiaSolarPanel.infinity_solar_panel;
    }

    public BlockTileEntity getBlock() {
        return Config.AvaritiaLoaded ? AvaritiaIntegration.blockAvSolarPanel : null;
    }

}
