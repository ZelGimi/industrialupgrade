package com.denfop.integration.de;

import com.denfop.Config;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;

public class TileAwakenedSolarPanel extends TileSolarPanel {

    public TileAwakenedSolarPanel() {
        super(EnumSolarPanels.AWAKENED_SOLAR_PANEL);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockDESolarPanel.awakened_solar_panel;
    }

    public BlockTileEntity getBlock() {
        return Config.DraconicLoaded ? DraconicIntegration.blockDESolarPanel : null;
    }

}
