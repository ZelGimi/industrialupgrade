package com.denfop.integration.botania;


import com.denfop.Config;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;

public class TileTerrasteelSolarPanel extends TileSolarPanel {

    public TileTerrasteelSolarPanel() {
        super(EnumSolarPanels.TERRASTEEL_SOLAR_PANEL);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBotSolarPanel.terrasteel_solar_panel;
    }

    public BlockTileEntity getBlock() {
        return Config.BotaniaLoaded ? BotaniaIntegration.blockBotSolarPanel : null;
    }

}
