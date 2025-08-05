package com.denfop.tiles.base;


import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAdminPanel;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileAdminSolarPanel extends TileSolarPanel {

    public TileAdminSolarPanel(BlockPos pos, BlockState state) {
        super(14, EnumSolarPanels.QUARK_SOLAR_PANEL.genday * 4, EnumSolarPanels.QUARK_SOLAR_PANEL.producing * 4,
                EnumSolarPanels.QUARK_SOLAR_PANEL.maxstorage * 16, null, BlockAdminPanel.admpanel, pos, state
        );
    }

    @Override
    public boolean canRender() {
        return false;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockAdminPanel.admpanel;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockadmin.getBlock();
    }

}
