package com.denfop.integration.thaumcraft;


import com.denfop.Config;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;

public class TileThaumSolarPanel extends TileSolarPanel {

    public TileThaumSolarPanel() {
        super(EnumSolarPanels.THAUM_SOLAR_PANEL);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockThaumSolarPanel.thaum_solar_panel;
    }

    public BlockTileEntity getBlock() {
        return Config.Thaumcraft ? ThaumcraftIntegration.blockThaumSolarPanel : null;
    }

}
