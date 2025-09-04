package com.denfop.blockentity.panels.overtime;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.blockentity.panels.entity.EnumSolarPanels;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolarPanelsEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGravitonSolarPanel extends BlockEntitySolarPanel {

    public BlockEntityGravitonSolarPanel(BlockPos pos, BlockState state) {
        super(EnumSolarPanels.GRAVITON_SOLAR_PANEL, BlockSolarPanelsEntity.graviton_solar_panel, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockSolarPanelsEntity.graviton_solar_panel;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockpanel.getBlock(getTeBlock().getId());
    }

}

