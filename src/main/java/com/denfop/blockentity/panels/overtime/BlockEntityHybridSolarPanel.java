package com.denfop.blockentity.panels.overtime;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.blockentity.panels.entity.EnumSolarPanels;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolarPanelsEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityHybridSolarPanel extends BlockEntitySolarPanel {


    public BlockEntityHybridSolarPanel(BlockPos pos, BlockState state) {
        super(EnumSolarPanels.HYBRID_SOLAR_PANEL, BlockSolarPanelsEntity.hybrid_solar_paneliu, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockSolarPanelsEntity.hybrid_solar_paneliu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockpanel.getBlock(getTeBlock().getId());
    }

}
