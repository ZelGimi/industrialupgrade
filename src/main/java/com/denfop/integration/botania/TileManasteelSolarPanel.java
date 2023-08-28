package com.denfop.integration.botania;


import com.denfop.Config;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;

public class TileManasteelSolarPanel extends TileSolarPanel {

    public TileManasteelSolarPanel() {
        super(EnumSolarPanels.MANASTEEL_SOLAR_PANEL);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBotSolarPanel.manasteel_solar_panel;
    }

    public BlockTileEntity getBlock() {
        return Config.BotaniaLoaded ? BotaniaIntegration.blockBotSolarPanel : null;
    }

}
