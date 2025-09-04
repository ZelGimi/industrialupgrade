package com.denfop.blockentity.mechanism.generator.energy;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.blockentity.panels.entity.EnumSolarPanels;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySolarGenerator extends BlockEntitySolarPanel {


    public BlockEntitySolarGenerator(BlockPos pos, BlockState state) {
        super(EnumSolarPanels.SOLAR_PANEL_DEFAULT, BlockBaseMachine3Entity.solar_iu, pos, state);
    }


    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.solar_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
