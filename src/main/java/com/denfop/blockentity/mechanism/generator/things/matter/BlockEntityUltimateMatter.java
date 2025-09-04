package com.denfop.blockentity.mechanism.generator.things.matter;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMatter;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachineEntity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityUltimateMatter extends BlockEntityMultiMatter {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityUltimateMatter(BlockPos pos, BlockState state) {
        super(700000F, 16, 256000000, BlockBaseMachineEntity.per_matter, pos, state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.011));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.045));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachineEntity.per_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock().getId());
    }
}
