package com.denfop.blockentity.base;


import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.blockentity.panels.entity.EnumSolarPanels;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAdminPanelEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdminSolarPanel extends BlockEntitySolarPanel {

    public BlockEntityAdminSolarPanel(BlockPos pos, BlockState state) {
        super(14, EnumSolarPanels.QUARK_SOLAR_PANEL.genday * 4, EnumSolarPanels.QUARK_SOLAR_PANEL.producing * 4,
                EnumSolarPanels.QUARK_SOLAR_PANEL.maxstorage * 16, null, BlockAdminPanelEntity.admpanel, pos, state
        );
    }

    @Override
    public boolean canRender() {
        return false;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockAdminPanelEntity.admpanel;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockadmin.getBlock();
    }

}
