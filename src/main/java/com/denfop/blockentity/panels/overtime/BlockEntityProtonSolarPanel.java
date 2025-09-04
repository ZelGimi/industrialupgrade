package com.denfop.blockentity.panels.overtime;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.blockentity.panels.entity.EnumSolarPanels;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolarPanelsEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityProtonSolarPanel extends BlockEntitySolarPanel {


    public BlockEntityProtonSolarPanel(BlockPos pos, BlockState state) {
        super(EnumSolarPanels.PROTON_SOLAR_PANEL, BlockSolarPanelsEntity.proton_solar_panel, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockSolarPanelsEntity.proton_solar_panel;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockpanel.getBlock(getTeBlock().getId());
    }

}
