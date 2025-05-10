package com.denfop.tiles.panels.overtime;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolarPanels;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileGravitonSolarPanel extends TileSolarPanel {

    public TileGravitonSolarPanel(BlockPos pos, BlockState state) {
        super(EnumSolarPanels.GRAVITON_SOLAR_PANEL, BlockSolarPanels.graviton_solar_panel, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockSolarPanels.graviton_solar_panel;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockpanel.getBlock(getTeBlock().getId());
    }

}

