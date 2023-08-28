package com.denfop.integration.de;

import com.denfop.Config;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;

public class TileChaoticSolarPanel extends TileSolarPanel {

    public TileChaoticSolarPanel() {
        super(EnumSolarPanels.CHAOTIC_SOLAR_PANEL);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockDESolarPanel.chaotic_solar_panel;
    }

    public BlockTileEntity getBlock() {
        return Config.DraconicLoaded ? DraconicIntegration.blockDESolarPanel : null;
    }

}
