package com.denfop.integration.avaritia;


import com.denfop.Config;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;

public class TileNeutronSolarPanel extends TileSolarPanel {

    public TileNeutronSolarPanel() {
        super(EnumSolarPanels.NEUTRONIUM_SOLAR_PANEL_AVARITIA);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockAvaritiaSolarPanel.neutron_solar_panel_av;
    }

    public BlockTileEntity getBlock() {
        return Config.AvaritiaLoaded ? AvaritiaIntegration.blockAvSolarPanel : null;
    }

}
